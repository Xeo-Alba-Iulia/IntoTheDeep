package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.subsystems.util.CancelableAction

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] instance from OpMode
 */
@Config
class Lift(hardwareMap: HardwareMap) : CancelableAction {
    companion object {
        @JvmField
        var kV = 0.3

        fun staticPower(power: Double) = (power + kV * 3.0) / 4.0
    }

    private val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    private val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    private var isCanceled = false

    private val liftMotors = listOf(liftLeft, liftRight)

    init {
        liftMotors.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
        liftLeft.direction = DcMotorSimple.Direction.REVERSE
    }

    var power = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
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

//        val positionVelocityPair = encoder.getPositionAndVelocity()
//        measuredPosition = positionVelocityPair.position.toDouble()
//        measuredVelocity = positionVelocityPair.velocity.toDouble()
//
//        power = controller.update(measuredPosition, measuredVelocity)
//
//        if (isVerbose) {
//            p.putAll(
//                mapOf(
//                    "liftPosition" to measuredPosition,
//                    "liftVelocity" to measuredVelocity,
//                    "liftPower" to power
//                )
//            )
//        }

        for (motor in liftMotors) {
            motor.power = power
        }

        p.put("Lift Power", power)

        return true
    }
}