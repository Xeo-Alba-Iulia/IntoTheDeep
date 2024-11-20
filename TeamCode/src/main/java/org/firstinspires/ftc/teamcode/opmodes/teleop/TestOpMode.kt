package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.hardware.MovementHardware

@TeleOp
class TestOpMode : OpMode() {
    lateinit var movementHardware: MovementHardware
    override fun init() {
        movementHardware = MovementHardware(hardwareMap)
    }

    fun Boolean.toDouble() = if (this) 1.0 else 0.0

    override fun loop() {
        if (gamepad1 == null)
            return

        if (gamepad1.dpad_up) {
            movementHardware.setPower(1.0)
            return
        } else if (gamepad1.dpad_down) {
            movementHardware.setPower(-1.0)
            return
        } else {
            movementHardware.setPower(0.0)
        }
        val gamepad = gamepad1 as Gamepad

        movementHardware.apply {
            frontLeft.power = gamepad.y.toDouble()
            frontRight.power = gamepad.b.toDouble()
            backRight.power = gamepad.a.toDouble()
            backLeft.power = gamepad.x.toDouble()
        }
    }
}