package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.IntakeRotationPosition
import org.firstinspires.ftc.teamcode.util.PendulServoPosition

@TeleOp(name = "Servo TeleOp", group = "TeleOp")
class ServoTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val dashboard = FtcDashboard.getInstance()

        waitForStart()

        val servoPositions = listOf(
            PendulServoPosition(hardwareMap),
            IntakeRotationPosition(hardwareMap)
        )

        while (opModeIsActive()) {
            servoPositions.forEach { it.update() }
            dashboard.telemetry.update()
        }
    }
}