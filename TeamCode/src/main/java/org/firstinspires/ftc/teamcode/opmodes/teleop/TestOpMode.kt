package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.RobotHardware

@TeleOp
class TestOpMode : OpMode() {
    lateinit var robot: RobotHardware

    override fun init() {
        robot = RobotHardware(hardwareMap)
    }

    fun Boolean.toDouble() = if (this) 1.0 else 0.0

    override fun loop() {
        if (gamepad1 == null)
            return

        if (gamepad1.dpad_down) {
            robot.motors.forEach { it.power = 1.0 }
            return
        }
        val gamepad = gamepad1 as Gamepad

        robot.frontLeft.power = gamepad.y.toDouble()
        robot.frontRight.power = gamepad.b.toDouble()
        robot.backRight.power = gamepad.a.toDouble()
        robot.backLeft.power = gamepad.x.toDouble()
    }
}