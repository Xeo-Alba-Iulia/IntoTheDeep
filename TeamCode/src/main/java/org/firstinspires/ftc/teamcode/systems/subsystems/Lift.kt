package org.firstinspires.ftc.teamcode.systems.subsystems

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.PIDCoefficients
import org.firstinspires.ftc.teamcode.control.PIDFController
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualPositionMechanism

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
    }

    val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    val encoder = RawEncoder(liftRight)

    private var isCanceled = false

    val lift = listOf(liftLeft, liftRight)

    val controller = PIDFController(coefficients, kV, kA, kStatic)

    override var targetPosition
        get() = controller.targetPosition
        set(value) {
            controller.targetPosition = value
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
        lift.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
        encoder.direction = DcMotorSimple.Direction.REVERSE

        controller.targetVelocity = 10.0
    }

    private var power = 0.0
        set(value) {
            field = value
            liftRight.power = value
        }

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

        power = controller.update(measuredPosition, measuredVelocity)

            p.putAll(
                mapOf(
                    "liftPosition" to measuredPosition,
                    "liftVelocity" to measuredVelocity,
                    "liftPower" to power
                )
            )

        return true
    }
}