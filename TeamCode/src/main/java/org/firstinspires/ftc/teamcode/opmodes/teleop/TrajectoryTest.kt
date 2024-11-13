package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.trajectoryAction

@Autonomous
class TrajectoryTest : LinearOpMode() {
    override fun runOpMode() {

        val beginPose = Pose2d(0.0, 0.0, 0.0)
        val drive = MecanumDrive(hardwareMap, beginPose)

        drive.trajectoryAction(beginPose) {
            lineToX(10.0)
            lineToY(10.0)
        }

        waitForStart()
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running")
            telemetry.update()
        }
    }
}