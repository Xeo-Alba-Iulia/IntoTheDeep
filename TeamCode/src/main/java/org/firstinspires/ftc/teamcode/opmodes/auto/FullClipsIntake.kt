package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.PathBuilder
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@Autonomous
class FullClipsIntake : LinearOpMode() {
    val beginPose = Pose(9.0, 48.0, 0.0)
    val samplePoints =
        arrayOf(
            Point(29.0, 41.2),
            Point(29.0, 30.8),
            Point(29.0, 20.4)
        )

    val sampleAngle = -Math.toRadians(47.0)
    val dropAngle = -Math.toRadians(130.0)

    private val pathTimer = Timer()
    private var state = 0
        set(value) {
            field = value
            pathTimer.resetTimer()
        }

    override fun runOpMode() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        val follower = Follower(hardwareMap)
        follower.setStartingPose(beginPose)

        val dashboard = FtcDashboard.getInstance()
        val robot = RobotHardware(hardwareMap)

        val pickupSamples =
            arrayOf(
                PathBuilder()
                    .addBezierLine(Point(beginPose), samplePoints[0])
                    .setLinearHeadingInterpolation(beginPose.heading, sampleAngle)
                    .build(),
                PathBuilder()
                    .addBezierLine(samplePoints[0], samplePoints[1])
                    .setLinearHeadingInterpolation(dropAngle, sampleAngle)
                    .build(),
                PathBuilder()
                    .addBezierLine(samplePoints[1], samplePoints[2])
                    .setLinearHeadingInterpolation(dropAngle, sampleAngle, 0.4)
                    .build()
            )

        val lastDropPoint = Point(24.0, 28.0)

        val lastDrop =
            PathBuilder()
                .addBezierLine(samplePoints[2], lastDropPoint)
                .setLinearHeadingInterpolation(sampleAngle, dropAngle)
                .build()

        waitForStart()

        val opModeTimer = Timer()

        while (!isStopRequested) {
            when (state) {
                0 -> {
                    follower.followPath(pickupSamples[0])
                    state = 1
                }

                1 -> {
                    robot.claw.isClosed = false
                    robot.intake.targetPosition = IntakePositions.PICKUP
                    robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.left
                    if (!follower.isBusy) {
                        robot.intake.pickUp()
                        state = 2
                    }
                }

                2 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.turnDegrees(85.0, false)
                        state = 3
                    }
                }

                3 -> {
                    if (pathTimer.elapsedTimeSeconds > 1.0) {
                        robot.intake.claw.isClosed = false
                        follower.followPath(pickupSamples[1], 0.8, true)
                        state = 4
                    }
                }

                14 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.1) {
                        follower.followPath(pickupSamples[1])
                        state = 4
                    }
                }

                4 -> {
                    if (!follower.isBusy) {
                        robot.intake.pickUp()
                        state = 5
                    }
                }

                5 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.turnTo(dropAngle)
                        state = 6
                    }
                }

                6 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        robot.intake.claw.isClosed = false
                        follower.followPath(pickupSamples[2])
                        state = 7
                    }
                }

                7 -> {
                    if (!follower.isBusy) {
                        robot.intake.pickUp()
                        state = 8
                    }
                }

                8 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(lastDrop)
                        state = 9
                    }
                }

                9 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        robot.intake.claw.isClosed = false
                        state = 10
                    }
                }
            }

            robot.run(TelemetryPacket())
            follower.update()
        }
    }
}
