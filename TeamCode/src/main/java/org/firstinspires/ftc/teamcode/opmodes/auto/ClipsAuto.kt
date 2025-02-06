package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.*
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@Autonomous
class ClipsAuto : LinearOpMode() {
    private val pathTimer = Timer()
    private var state = 0
        set(value) {
            pathTimer.resetTimer()
            field = value
        }

    override fun runOpMode() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)

        val follower = Follower(hardwareMap)

        val robot = RobotHardware(hardwareMap)

        val dashboard = FtcDashboard.getInstance()

        val beginPose = Pose(8.2, 64.5, Math.toRadians(180.0))
        val scorePose = Pose(38.5, 70.0, Math.toRadians(180.0))
        val pickup1Intermediary = Pose(36.0, 36.0, 0.0)
        val pickup1Pose = Pose(60.3, 29.06, 0.0)
        val drop1Pose = Pose(18.0, 28.0, 0.0)
        val pickup2Pose = Pose(60.3, 20.5, 0.0)
        val pickup2Intermediary = Point(62.3, 30.0)
        val drop2Pose = Pose(18.0, 20.5, 0.0)
        val pickup3Pose = Pose(60.3, 13.0, 0.0)
        val pickup3Intermediary = Point(62.3, 20.0)
        val drop3Pose = Pose(18.0, 13.0, 0.0)

        val scorePreloadPath = Path(BezierLine(Point(beginPose), Point(scorePose)))
        scorePreloadPath.setConstantHeadingInterpolation(Math.toRadians(180.0))

        val drop1Path =
            follower
                .pathBuilder()
                .addPath(
                    BezierCurve(Point(scorePose), Point(14.0, 44.5), Point(pickup1Intermediary))
                ).setLinearHeadingInterpolation(Math.toRadians(180.001), 0.0)
                .addPath(BezierCurve(Point(pickup1Intermediary), Point(62.6, 38.8), Point(pickup1Pose)))
                .setConstantHeadingInterpolation(0.0)
                .addPath(BezierLine(Point(pickup1Pose), Point(drop1Pose)))
                .setConstantHeadingInterpolation(0.0)
                .build()

        val drop2Path =
            follower
                .pathBuilder()
                .addPath(BezierCurve(Point(drop1Pose), pickup2Intermediary, Point(pickup2Pose)))
                .setConstantHeadingInterpolation(0.0)
                .addPath(BezierLine(Point(pickup2Pose), Point(drop2Pose)))
                .setConstantHeadingInterpolation(0.0)
                .build()

        val drop3Path =
            follower
                .pathBuilder()
                .addPath(BezierCurve(Point(drop2Pose), pickup3Intermediary, Point(pickup3Pose)))
                .setConstantHeadingInterpolation(0.0)
                .addPath(BezierLine(Point(pickup3Pose), Point(drop3Pose)))
                .setConstantHeadingInterpolation(0.0)
                .build()

        follower.setStartingPose(beginPose)

        waitForStart()

        state = 0

        while (!isStopRequested) {
            val p = TelemetryPacket()
            robot.run(p)
            dashboard.sendTelemetryPacket(p)
            dashboard.telemetry.update()

            when (state) {
                0 -> {
                    robot.lift.targetPosition = Positions.Lift.half - 15.0
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                    if (pathTimer.elapsedTimeSeconds >= 0.5) {
                        follower.followPath(scorePreloadPath, true)
                        state = 2
                    }
                }

                2 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        robot.lift.targetPosition = Positions.Lift.down

                        follower.followPath(drop1Path)
                        state = 3
                    }
                }

                3 -> {
                    if (pathTimer.elapsedTimeSeconds >= 1.0) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }

                    if (!follower.isBusy) {
                        follower.followPath(drop2Path)
                        state = 4
                    }
                }

                4 -> {
                    if (!follower.isBusy) {
                        follower.followPath(drop3Path)
                        state = 5
                    }
                }
            }

            follower.update()
            follower.drawOnDashBoard()
        }
    }
}
