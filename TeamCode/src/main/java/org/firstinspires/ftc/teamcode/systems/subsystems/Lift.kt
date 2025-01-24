package org.firstinspires.ftc.teamcode.systems.subsystems

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.PIDCoefficients
import org.firstinspires.ftc.teamcode.control.PIDFController
import org.firstinspires.ftc.teamcode.profile.MotionProfileGenerator
import org.firstinspires.ftc.teamcode.profile.MotionState
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualPositionMechanism
import org.firstinspires.ftc.teamcode.util.absoluteDistance
import org.firstinspires.ftc.teamcode.util.epsilonEquals

private const val maxPIDDistance = 200.0

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] object from the OpMode
 */
@Config
class Lift(hardwareMap: HardwareMap) : ManualPositionMechanism {
    companion object {
        @JvmField
        @Volatile
        var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

        @JvmField
        @Volatile
        var kStatic = 0.0
        @JvmField
        @Volatile
        var kV = 0.0
        @JvmField
        @Volatile
        var kA = 0.0

        @JvmField
        @Volatile
        var maxVel = 30.0

        @JvmField
        @Volatile
        var maxAccel = 15.0

        @JvmField
        @Volatile
        var maxJerk = 0.0
    }

    private val liftLeft = motorEncoderPair(hardwareMap, "LiftLeft")
    private val liftRight = motorEncoderPair(hardwareMap, "LiftRight")

    private var isCanceled = false

    private val lifts = arrayOf(liftLeft, liftRight)

    private val controller = PIDFController(coefficients, kV, kA, kStatic)

    private var profile = MotionProfileGenerator.generateSimpleMotionProfile(
        MotionState(0.0, 0.0),
        MotionState(0.0, 0.0),
        maxVel,
        maxAccel,
        maxJerk
    )

    override var targetPosition
        get() = profile.end().x
        set(value) {
            require(value in 0.0..1500.0) { "target position $value is invalid" }
            if (value epsilonEquals profile.end().x) return // Repeated set from TeleOP
            profile = MotionProfileGenerator.generateSimpleMotionProfile(
                MotionState(measuredPosition, measuredVelocity),
                MotionState(value, 0.0),
                maxVel,
                maxAccel,
                maxJerk
            )
        }

    /**
     * Actual position of the lift, as measured while the action is running
     */
    var measuredPosition = 0.0
        private set

    /**
     * Measured velocity of the lift, as measured while the action is running
     */
    var measuredVelocity = 0.0
        private set

    val busy
        get() = absoluteDistance(measuredPosition, targetPosition) < 10 && measuredPosition epsilonEquals 0.0

    init {
        lifts.forEach {
            it.first.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }

        liftRight.first.direction = DcMotorSimple.Direction.REVERSE

        controller.setInputBounds(0.0, 1500.0)
        controller.setOutputBounds(-1.0, 1.0)
    }

    var power = 0.0
        private set

    override fun cancel() {
        isCanceled = true
    }

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false
            power = 0.0

            return false
        }

        measuredPosition = 0.0
        measuredVelocity = 0.0
        power = 0.0

        for (lift in lifts) {
            val positionVelocityPair = lift.second.getPositionAndVelocity()
            val measuredPosition = positionVelocityPair.position.toDouble()
            val measuredVelocity = positionVelocityPair.velocity.toDouble()

            controller.apply {
                val state = profile[measuredPosition]

                targetPosition = state.x
                targetVelocity = state.v
                targetAcceleration = state.a
            }

            lift.first.power = controller.update(measuredPosition, measuredVelocity)

            this.measuredPosition += measuredPosition
            this.measuredVelocity += measuredVelocity
            this.power += lift.first.power
        }

        measuredPosition /= lifts.size
        measuredVelocity /= lifts.size
        power /= lifts.size

        p.putAll(
            mapOf(
                "liftPosition" to measuredPosition,
                "liftVelocity" to measuredVelocity,
                "liftPower" to power,
                "liftProfile" to profile,
            )
        )

        return true
    }
}

private fun motorEncoderPair(hardwareMap: HardwareMap, name: String): Pair<DcMotorEx, RawEncoder> {
    val motor = hardwareMap.dcMotor[name] as DcMotorEx
    val encoder = RawEncoder(motor)
    return motor to encoder
}

@TeleOp(name = "Lift Test")
class LiftTest : ManualMechanismTeleOp(::Lift) {
    override fun loop() {
        if (gamepad1.a) {
            manualPositionMechanism.targetPosition = 1000.0
        } else if (gamepad1.b) {
            manualPositionMechanism.targetPosition = 0.0
        }
        this.telemetry.addData("isBusy", (manualPositionMechanism as Lift)::busy::get)
        super.loop()
    }
}