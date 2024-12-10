package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.hardware.Movement

@TeleOp
class TestOpMode : OpMode() {
    lateinit var movement: Movement
    override fun init() {
        movement = Movement(hardwareMap)
    }

    fun Boolean.toDouble() = if (this) 1.0 else 0.0

    override fun loop() {
        if (gamepad1 == null)
            return

        if (gamepad1.dpad_up) {
            movement.setPower(1.0)
            return
        } else if (gamepad1.dpad_down) {
            movement.setPower(-1.0)
            return
        } else {
            movement.setPower(0.0)
        }
        val gamepad = gamepad1 as Gamepad

        movement.apply {
            frontLeft.power = gamepad.y.toDouble()
            frontRight.power = gamepad.b.toDouble()
            backRight.power = gamepad.a.toDouble()
            backLeft.power = gamepad.x.toDouble()
        }
    }
}