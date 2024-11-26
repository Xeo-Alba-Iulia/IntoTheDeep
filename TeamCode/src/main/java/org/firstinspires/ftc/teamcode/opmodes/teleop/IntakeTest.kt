package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Intake
import kotlin.properties.Delegates

@TeleOp
class IntakeTest : LinearOpMode() {
    companion object {
        private const val MULTIPLIER = 0.003
    }

    override fun runOpMode() {
        val intake = Intake(hardwareMap)

        waitForStart()

//        runBlocking(ParallelAction(intake.start(), SequentialAction(
//            SleepAction(1.0),
//            intake.setPower(0.5),
//            SleepAction(1.0),
//            intake.stop()
//        )))

        var position: Double by Delegates.vetoable(0.0) { _, _, new -> new in 0.0..1.0 }

        val dash = FtcDashboard.getInstance()
        var isRunning = true

        while (opModeIsActive() && isRunning) {
            val packet = TelemetryPacket()

            position += MULTIPLIER * gamepad1.left_stick_y
            intake.rotate.position = position
            isRunning = intake.rotate.run(packet)

            dash.sendTelemetryPacket(packet)
        }
    }
}