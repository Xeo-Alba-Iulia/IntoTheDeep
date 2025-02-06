package org.firstinspires.ftc.teamcode.opmodes.auto

import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.*
import com.pedropathing.util.Constants
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@Autonomous
class ClipsAuto : LinearOpMode() {
    override fun runOpMode() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)

        val follower = Follower(hardwareMap)

        val beginPose = Pose(8.2, 64.5, Math.toRadians(180.0))
        val scorePose = Pose(38.5, 70.0, Math.toRadians(180.0))
        val pickup1Intermediary = Pose(36.0, 36.0, 0.0)
        val pickup1Pose = Pose(60.3, 25.7, 0.0)

        val scorePreloadPath = Path(BezierLine(Point(beginPose), Point(scorePose)))
        scorePreloadPath.setConstantHeadingInterpolation(Math.toRadians(180.0))

        val pickup1PathChain =
            follower
                .pathBuilder()
                .addPath(
                    BezierCurve(Point(scorePose), Point(14.0, 44.5), Point(pickup1Intermediary))
                ).setLinearHeadingInterpolation(Math.toRadians(180.001), 0.0)
                .addPath(BezierCurve(Point(pickup1Intermediary), Point(62.6, 38.8), Point(pickup1Pose)))
                .setConstantHeadingInterpolation(0.0)
                .build()

        waitForStart()

        var state = 0

        while (!isStopRequested) {
            when (state) {
                0 -> {
                    follower.followPath(scorePreloadPath)
                    state = 1
                }

                1 -> {
                    if (follower.isBusy) {
                        return
                    }
                    follower.followPath(pickup1PathChain)
                    state = 2
                }
            }
        }
    }
}
