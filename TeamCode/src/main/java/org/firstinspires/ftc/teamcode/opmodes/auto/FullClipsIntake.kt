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
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@Autonomous
class FullClipsIntake : LinearOpMode() {
    val beginPose = Pose(9.0, 60.0, Math.toRadians(180.0))
    val samplePoints =
        arrayOf(
            Point(29.0, 41.2),
            Point(29.0, 31.0),
            Point(29.0, 21.0)
        )

    val scorePose =
        arrayOf(
            Point(40.0, 68.0),
            Point(38.0, 68.5),
            Point(38.0, 69.0),
            Point(38.0, 69.5),
            Point(38.0, 70.0)
        )

    val scoreAngle = Math.toRadians(180.0)

    val scoreControl = Point(16.0, 70.0)

    val pickupSpecimen = Pose(16.8, 28.0, 0.0)
    val pickupControl =
        arrayOf(
            Point(24.0, 70.0),
            Point(36.0, 36.0)
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

        val intake = Intake(hardwareMap)

        val scorePreload =
            PathBuilder()
                .addBezierLine(Point(beginPose), scorePose[0])
                .setConstantHeadingInterpolation(Math.toRadians(180.0))
                .build()

        val pickupSamples =
            arrayOf(
                PathBuilder()
                    .addBezierCurve(scorePose[0], Point(20.0, 60.0), samplePoints[0])
                    .setLinearHeadingInterpolation(beginPose.heading, sampleAngle)
                    .addParametricCallback(0.3) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }.addParametricCallback(0.7) {
                        robot.intake.targetPosition = IntakePositions.PICKUP
                        robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.left
                    }.build(),
                PathBuilder()
                    .addBezierLine(samplePoints[0], samplePoints[1])
                    .setLinearHeadingInterpolation(dropAngle, sampleAngle, 0.4)
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

        val pickupFromDrop =
            PathBuilder()
                .addBezierLine(lastDropPoint, Point(pickupSpecimen))
                .setLinearHeadingInterpolation(dropAngle, 0.0, 0.5)
                .build()

        val firstPickup =
            PathBuilder()
                .addBezierCurve(scorePose[0], pickupControl[0], pickupControl[1], Point(pickupSpecimen))
                .addParametricCallback(0.5) {
                    robot.outtake.outtakePosition = OuttakePosition.PICKUP
                }.setLinearHeadingInterpolation(scoreAngle, 0.0, 0.8)
                .build()

        val secondScore =
            arrayOf(
                PathBuilder()
                    .addBezierCurve(Point(pickupSpecimen), scoreControl, scorePose[1])
                    .setLinearHeadingInterpolation(0.0, scoreAngle, 0.8)
                    .addParametricCallback(0.4) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                    }.build(),
                PathBuilder()
                    .addBezierCurve(Point(pickupSpecimen), scoreControl, scorePose[2])
                    .setLinearHeadingInterpolation(0.0, scoreAngle, 0.8)
                    .addParametricCallback(0.4) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                    }.build(),
                PathBuilder()
                    .addBezierCurve(Point(pickupSpecimen), scoreControl, scorePose[3])
                    .setLinearHeadingInterpolation(0.0, scoreAngle, 0.8)
                    .addParametricCallback(0.4) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                    }.build(),
                PathBuilder()
                    .addBezierCurve(Point(pickupSpecimen), scoreControl, scorePose[4])
                    .setLinearHeadingInterpolation(0.0, scoreAngle, 0.8)
                    .addParametricCallback(0.4) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                    }.build()
            )

        intake.targetPosition = IntakePositions.TRANSFER

        while (opModeInInit()) {
            val packet = TelemetryPacket()
            intake.run(packet)
        }

        val opModeTimer = Timer()

        while (!isStopRequested) {
            when (state) {
                0 -> {
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                    robot.lift.targetPosition = Positions.Lift.half
                    robot.claw.isClosed = true
                    state = 50
                }

                50 -> {
                    follower.followPath(scorePreload)
                    state = 51
                }

                51 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        robot.lift.targetPosition = Positions.Lift.down
                        follower.followPath(pickupSamples[0])
                        state = 1
                    }
                }

                1 -> {
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
                        follower.turnDegrees(85.0, false)
                        state = 6
                    }
                }

                6 -> {
                    if (pathTimer.elapsedTimeSeconds > 1.0) {
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
                    if (!follower.isBusy) {
                        robot.intake.claw.isClosed = false
                        robot.intake.targetPosition = IntakePositions.TRANSFER
                        follower.followPath(pickupFromDrop)
                        state = 91
                    }
                }

                91 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[0])
                        state = 10
                    }
                }

                10 -> {
                    if (!follower.isBusy) {
                        robot.lift.targetPosition = Positions.Lift.down
                        robot.outtake.pendul.targetPosition = 0.7
                        robot.claw.isClosed = false
                        follower.followPath(firstPickup)
                        state = 11
                    }
                }

                11 -> {
                    if (!follower.isBusy) { // Score first specimen
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[1])
                        firstPickup.resetCallbacks()
                        state = 12
                    }
                }

                12 -> {
                    if (!follower.isBusy) {
                        robot.lift.targetPosition = Positions.Lift.down
                        robot.outtake.pendul.targetPosition = 0.7
                        robot.claw.isClosed = false
                        follower.followPath(firstPickup)
                        state = 13
                    }
                }

                13 -> {
                    if (!follower.isBusy) { // Score second specimen
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[2])
                        firstPickup.resetCallbacks()
                        state = 14
                    }
                }

                14 -> {
                    if (!follower.isBusy) {
                        robot.lift.targetPosition = Positions.Lift.down
                        robot.outtake.pendul.targetPosition = 0.7
                        robot.claw.isClosed = false
                        follower.followPath(firstPickup)
                        state = 15
                    }
                }

                15 -> {
                    if (!follower.isBusy) { // Score third specimen
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[3])
                        firstPickup.resetCallbacks()
                        state = 16
                    }
                }
            }

            robot.run(TelemetryPacket())
            follower.update()
        }
    }
}
