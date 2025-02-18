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
class FullClips : LinearOpMode() {
    override fun runOpMode() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        val follower = Follower(hardwareMap)
        follower.setStartingPose(beginPose)

        val dashboard = FtcDashboard.getInstance()
        val robot = RobotHardware(hardwareMap)

        val scorePreloadPath =
            PathBuilder()
                .addPath(BezierLine(beginPose, scorePose[0]))
                .setConstantHeadingInterpolation(scorePose[0].heading)
                .build()

        val dropAllSpecimens =
            PathBuilder()
                .dropFirstSpecimen()
                .addTemporalCallback(0.5) {
                    robot.outtake.outtakePosition = OuttakePosition.PICKUP
                }.dropSecondSpecimen()
                .dropThirdSpecimen()
                .build()

        val scoreFirstSpecimen =
            PathBuilder()
                .addBezierCurve(
                    Point(backThirdSample),
                    scoreControl,
                    Point(scorePose[1])
                ).setLinearHeadingInterpolation(backThirdSample.heading, scorePose[1].heading, 0.9)
                .addParametricCallback(0.3) {
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                }.build()

        val getSecondSpecimen =
            PathBuilder()
                .addBezierLine(Point(scorePose[1]), Point(pickupSpecimen))
                .setLinearHeadingInterpolation(scorePose[1].heading, pickupSpecimen.heading, 0.9)
                .addParametricCallback(0.5) {
                    robot.outtake.outtakePosition = OuttakePosition.PICKUP
                }.build()

        waitForStart()

        var state = 0
        val opModeTimer = Timer()

        while (!isStopRequested) {
            when (state) {
                0 -> {
                    robot.lift.targetPosition = Positions.Lift.half
                    robot.claw.isClosed = true
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                    if (opModeTimer.elapsedTimeSeconds > 0.15) {
                        follower.followPath(scorePreloadPath)
                        state = 1
                    }
                }

                1 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        robot.lift.targetPosition = Positions.Lift.down
                        follower.followPath(dropAllSpecimens)
                        state = 2
                    }
                }

                2 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(scoreFirstSpecimen)
                        state = 3
                    }
                }

                3 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        robot.lift.targetPosition = Positions.Lift.down
                        follower.followPath(getSecondSpecimen)
                        state = 4
                    }
                }

                4 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        state = 5
                    }
                }
            }

            follower.update()
            follower.drawOnDashBoard()

            val packet = TelemetryPacket()
            robot.run(packet)
            packet.put("State", state)
            dashboard.sendTelemetryPacket(packet)
        }
    }

    val beginPose = Pose(9.0, 60.0, Math.toRadians(180.0))

    val scoreControl = Point(21.0, 68.5)
    val pickupSpecimen = Pose(36.0, 12.0, 0.0)

    val frontFirstSample = Pose(57.7, 27.0, Math.toRadians(180.0))
    val backFirstSample = Pose(26.0, 27.0, Math.toRadians(180.0)) // FIXME: X mai mare aici

    val frontSecondSample = Pose(57.7, 17.0, 0.0)
    val backSecondSample = Pose(26.0, 17.0, 0.0)

    val frontThirdSample = Pose(57.7, 8.9, 0.0)
    val backThirdSample = Pose(12.0, 8.9, 0.0)

    @Suppress("ClassName")
    object scorePose {
        private val initialScorePose = Pose(41.5, 69.69, Math.toRadians(180.0))

        operator fun get(index: Int): Pose {
            val newPose = initialScorePose.copy()
            newPose.y += index * 0.5
            return newPose
        }
    }

    val dropFirstSpecimen: (PathBuilder.() -> PathBuilder) = {
        addPath(
            BezierCurve(
                Point(scorePose[0]),
                Point(19.5, 59.0),
                Point(35.0, 1.0),
                Point(65.0, 59.4),
                Point(frontFirstSample),
                Point(frontFirstSample)
            )
        )
        setLinearHeadingInterpolation(beginPose.heading, frontFirstSample.heading)
        addBezierLine(Point(frontFirstSample), Point(backFirstSample))
        setLinearHeadingInterpolation(frontFirstSample.heading, backFirstSample.heading)
        setZeroPowerAccelerationMultiplier(0.5)
    }

    val dropSecondSpecimen: (PathBuilder.() -> PathBuilder) = {
        val controlPoint = Point(67.5, 28.5)

        addPath(
            BezierCurve(
                Point(backFirstSample),
                controlPoint,
                Point(frontSecondSample)
            )
        )
        setLinearHeadingInterpolation(backFirstSample.heading, frontSecondSample.heading)
        addBezierLine(Point(frontSecondSample), Point(backSecondSample))
        setLinearHeadingInterpolation(frontSecondSample.heading, backSecondSample.heading)
    }

    val dropThirdSpecimen: (PathBuilder.() -> PathBuilder) = {
        val controlPoint = Point(67.0, 17.0)

        addBezierCurve(
            Point(backSecondSample),
            controlPoint,
            Point(frontThirdSample)
        )
        setLinearHeadingInterpolation(backSecondSample.heading, frontThirdSample.heading)
        addBezierLine(Point(frontThirdSample), Point(backThirdSample))
        setLinearHeadingInterpolation(frontThirdSample.heading, backThirdSample.heading)
    }
}
