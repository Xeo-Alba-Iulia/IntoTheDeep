package org.firstinspires.ftc.teamcode.subsystems

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
import org.firstinspires.ftc.teamcode.util.absoluteDistance

private const val PIDHeight = 100.0
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.ManualPositionMechanism

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] object from the OpMode
 * @param isVerbose whether to print debug information
 */
@Config
class Lift(hardwareMap: HardwareMap, private val isVerbose: Boolean = true) : ManualPositionMechanism {
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
        var maxJerk = 3.0
    }

    private val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    private val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    val encoder = RawEncoder(liftRight)

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

    //noinspection uninitialized_property_access
    override var targetPosition = 0.0
        set(value) {
            if (value == field)
                return

//            if (absoluteDistance(measuredPosition, value) > 100.0) {
                profile = MotionProfileGenerator.generateSimpleMotionProfile(
                    MotionState(measuredPosition, measuredVelocity),
                    MotionState(value, 0.0),
                    maxVel,
                    maxAccel,
                    maxJerk
                )
//            }
            field = value
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

    init {
        lifts.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
        encoder.direction = DcMotorSimple.Direction.REVERSE

        controller.setInputBounds(0.0, 1000.0)
        controller.setOutputBounds(-1.0, 1.0)
    }

    var power = 0.0

    override fun cancel() {
        isCanceled = true
    }

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false
            power = 0.0

            return false
        }

        val positionVelocityPair = encoder.getPositionAndVelocity()
        measuredPosition = positionVelocityPair.position.toDouble()
        measuredVelocity = positionVelocityPair.velocity.toDouble()

        controller.apply {
//            if (absoluteDistance(targetPosition, measuredPosition) > PIDHeight) {
                val state = profile[measuredPosition]

                targetPosition = state.x
                targetVelocity = state.v
                targetAcceleration = state.a
//            } else {
//                targetPosition = this@Lift.targetPosition
//
//                targetVelocity = 0.0
//                targetAcceleration = 0.0
//            }
        }

        power = controller.update(measuredPosition, measuredVelocity)

        if (isVerbose) {
            p.putAll(
                mapOf(
                    "liftPosition" to measuredPosition,
                    "liftVelocity" to measuredVelocity,
                    "liftPower" to power,
                    "liftProfile" to profile
                )
            )
        }

        return true
    }
}

@TeleOp
class LiftTest : ManualMechanismTeleOp(::Lift)