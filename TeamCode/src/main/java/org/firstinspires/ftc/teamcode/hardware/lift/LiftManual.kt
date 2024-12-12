package org.firstinspires.ftc.teamcode.hardware.lift

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
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

    val lift = listOf(liftLeft, liftRight)

    val controller = PIDFController(PIDCoefficients(0.01, 0.0, 0.0))

    /**
     * Target value for all pendul motors
     *
     * @see com.qualcomm.robotcore.hardware.DcMotor.getTargetPosition
     * @see com.qualcomm.robotcore.hardware.DcMotor.setTargetPosition
     */
    override var targetPosition = 0.0

    init {
        lift.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            it.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun run(p: TelemetryPacket): Boolean {

        p.put("Lift Left Position", liftLeft.currentPosition)
        p.put("Lift Right Position", liftRight.currentPosition)
    }
}