package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.util.Constants
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@TeleOp(name = "Intake Test", group = "B")
class IntakeTest : LinearOpMode() {
    override fun runOpMode() {
        val intake = Intake(hardwareMap)

        Constants.setConstants(FConstants::class.java, LConstants::class.java)

        waitForStart()

//        runBlocking(ParallelAction(intake.start(), SequentialAction(
//            SleepAction(1.0),
//            intake.setPower(0.5),
//            SleepAction(1.0),
//            intake.stop()
//        )))

        val dash = FtcDashboard.getInstance()

        val follower = Follower(hardwareMap)

        follower.startTeleopDrive()

        while (opModeIsActive()) {
            val packet = TelemetryPacket()

            follower.setTeleOpMovementVectors(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )
            follower.update()

            when {
                gamepad1.cross -> intake.targetPosition = IntakePositions.PICKUP
                gamepad1.triangle -> intake.targetPosition = IntakePositions.TRANSFER
            }

            when {
                gamepad1.right_bumper -> intake.pickUp()
                gamepad1.left_bumper -> intake.isClosed = false
            }

            intake.clawRotate.targetPosition +=
                (gamepad1.right_trigger - gamepad1.left_trigger) * intake.clawRotate.adjustMultiplier

            intake.run(packet)

            dash.sendTelemetryPacket(packet)
        }
    }
}
