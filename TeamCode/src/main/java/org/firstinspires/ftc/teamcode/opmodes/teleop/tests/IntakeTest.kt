package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakeMotor
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakePendul
import kotlin.properties.Delegates

@TeleOp(name = "Intake Test", group = "B")
class IntakeTest : LinearOpMode() {
    companion object {
        private const val MULTIPLIER = 0.003
    }

    override fun runOpMode() {
        val intake = IntakeMotor(hardwareMap)
        val intakePendul = IntakePendul(hardwareMap)

        waitForStart()

//        runBlocking(ParallelAction(intake.start(), SequentialAction(
//            SleepAction(1.0),
//            intake.setPower(0.5),
//            SleepAction(1.0),
//            intake.stop()
//        )))

        var rotationPosition: Double by Delegates.vetoable(0.0) { _, _, new -> new in 0.0..1.0 }
        var pendulPosition: Double by Delegates.vetoable(0.0) { _, _, new -> new in 0.0..1.0 }

        val dash = FtcDashboard.getInstance()
        var isRunning = true

        while (opModeIsActive()) {
            val packet = TelemetryPacket()

            rotationPosition += MULTIPLIER * gamepad1.left_stick_y
            pendulPosition += MULTIPLIER * gamepad1.right_stick_y

            intakePendul.targetPosition = pendulPosition

            intake.intakePower =
                when {
                    gamepad1.a -> 1.0
                    gamepad1.b -> -1.0
                    else -> 0.0
                }

            intake.run(packet)

            dash.sendTelemetryPacket(packet)
        }
    }
}
