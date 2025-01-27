package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.trajectoryAction

@Autonomous(name = "Trajectory test", group = "B")
class TrajectoryTest : LinearOpMode() {
    override fun runOpMode() {
        val beginPose = Pose2d(0.0, 0.0, 0.0)
        val drive = MecanumDrive(hardwareMap, beginPose)

        val action =
            drive.trajectoryAction(beginPose) {
                lineToX(10.0)
                lineToY(10.0)
            }

        val dashboard = FtcDashboard.getInstance()
        val telemetry = dashboard.telemetry

        waitForStart()
        telemetry.addData("Status", "Running")
        telemetry.update()

        runBlocking(action)

        telemetry.addData("Status", "Finished")
        telemetry.update()
    }
}
