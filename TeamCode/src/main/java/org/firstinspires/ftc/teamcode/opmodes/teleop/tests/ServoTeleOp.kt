package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.utils.ServoPosition

@TeleOp(name = "Servo TeleOp", group = "TeleOp")
class ServoTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val servoPendulLeft = hardwareMap.servo.get("PendulLeft")
        val servoPendulRight = hardwareMap.servo.get("PendulRight")

        servoPendulLeft.direction = Servo.Direction.REVERSE

        val dashboard = FtcDashboard.getInstance()

        waitForStart()

        val servoPos = ServoPosition(servoPendulLeft, servoPendulRight)

        while (opModeIsActive()) {
            servoPos.update()
            dashboard.telemetry.update()
        }
    }
}