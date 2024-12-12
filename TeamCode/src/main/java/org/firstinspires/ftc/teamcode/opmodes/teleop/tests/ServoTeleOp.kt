package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.IntakeRotationTest
import org.firstinspires.ftc.teamcode.util.LiftTest

@TeleOp(name = "Position Tests", group = "Tests")
class ServoTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val dashboard = FtcDashboard.getInstance()

        waitForStart()

        val servoPositions = listOf(
            IntakeRotationTest(hardwareMap),
            LiftTest(hardwareMap),
            PendulTest(hardwareMap)
        )

        while (opModeIsActive()) {
            val telemetryPacket = TelemetryPacket()
            servoPositions.forEach {
                it.updateFromDashboard()
                it.run(telemetryPacket)
            }

            dashboard.sendTelemetryPacket(telemetryPacket)
            dashboard.telemetry.update()
        }
    }
}