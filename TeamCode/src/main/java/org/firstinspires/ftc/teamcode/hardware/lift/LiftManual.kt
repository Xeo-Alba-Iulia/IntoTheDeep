package org.firstinspires.ftc.teamcode.hardware.lift

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.PIDCoefficients
import org.firstinspires.ftc.teamcode.control.PIDFController
import org.firstinspires.ftc.teamcode.hardware.ManualPositionMechanism

class LiftManual(hardwareMap: HardwareMap) : Action, ManualPositionMechanism {
    val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    // FIXME: Set the correct lift motor
    val encoder = RawEncoder(liftRight)

    private var isCanceled = false

    val lift = listOf(liftLeft, liftRight)

    val controller = PIDFController(PIDCoefficients(0.01, 0.0, 0.0))

    override var targetPosition = 0.0

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
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    private var power = 0.0
        set(value) {
            field = value
            lift.forEach { it.power = field }
        }

    fun cancel() {
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

        p.put("Lift Power", power)
        return true
    }
}