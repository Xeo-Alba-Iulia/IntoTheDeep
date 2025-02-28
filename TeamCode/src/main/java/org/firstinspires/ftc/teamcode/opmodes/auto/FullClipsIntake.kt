package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.PathBuilder
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.pedropathing.util.Drawing
import com.pedropathing.util.Timer
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.PositionStore
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants
import kotlin.math.abs

@Autonomous
class FullClipsIntake : LinearOpMode() {
    val beginPose = Pose(8.5, 60.0, Math.toRadians(180.0))
    val samplePoints =
        arrayOf(
            Point(30.0, 41.0),
            Point(30.7, 31.0),
            Point(31.5, 20.8),
        )

    val scorePose =
        arrayOf(
            Point(39.0, 65.0),
            Point(39.0, 68.0),
            Point(39.0, 70.0),
            Point(39.0, 71.0),
            Point(39.0, 73.0),
        )

    val scoreAngle = Math.toRadians(180.001)

    val scoreControl = Point(16.0, 70.0)

    val pickupSpecimen = Pose(15.5, 28.0, 0.0)
    val pickupControl =
        arrayOf(
            Point(24.0, 70.0),
            Point(36.0, 36.0),
        )

    val sampleAngle = Math.toRadians(313.0)
    val dropAngle = Math.toRadians(234.0)

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

        val hubs = hardwareMap.getAll(LynxModule::class.java)

        follower.setMaxPower(
            13.0 / hubs.map { it.getInputVoltage(VoltageUnit.VOLTS) }.average(),
        )

        val dashboard = FtcDashboard.getInstance()
        val robot = RobotHardware(hardwareMap)

        val intake = Intake(hardwareMap)
        val pendul = Pendul(hardwareMap)

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
                    .setLinearHeadingInterpolation(dropAngle, sampleAngle, 0.7)
                    .build(),
                PathBuilder()
                    .addBezierLine(samplePoints[1], samplePoints[2])
                    .setLinearHeadingInterpolation(dropAngle, sampleAngle, 0.4)
                    .build(),
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
            arrayOf(
                PathBuilder()
                    .addBezierCurve(scorePose[0], pickupControl[0], pickupControl[1], Point(16.0, 30.0))
                    .addParametricCallback(0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }.setLinearHeadingInterpolation(scoreAngle, 0.0, 0.75)
                    .build(),
                PathBuilder()
                    .addBezierCurve(scorePose[0], pickupControl[0], pickupControl[1], Point(16.0, 29.0))
                    .addParametricCallback(0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }.setLinearHeadingInterpolation(scoreAngle, 0.0, 0.75)
                    .build(),
                PathBuilder()
                    .addBezierCurve(scorePose[0], pickupControl[0], pickupControl[1], Point(16.5, 28.0))
                    .addParametricCallback(0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }.setLinearHeadingInterpolation(scoreAngle, 0.0, 0.75)
                    .build(),
            )

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
                    }.build(),
            )

        val park =
            PathBuilder()
                .addBezierLine(scorePose[4], Point(pickupSpecimen))
                .setLinearHeadingInterpolation(scoreAngle, Math.toRadians(230.0), 0.2)
                .build()

        intake.targetPosition = IntakePositions.TRANSFER
        pendul.targetPosition = 1.0

        while (opModeInInit()) {
            val packet = TelemetryPacket()
            intake.run(packet)
            pendul.run(packet)
            dashboard.sendTelemetryPacket(packet)
        }
        val pickupDelay = 0.12

        val opModeTimer = Timer()

        while (!isStopRequested) {
            if (opModeTimer.elapsedTimeSeconds > 29.7) {
                state = -1
            }

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
                        follower.turnDegrees(95.0, false)
                        state = 3
                    }
                }

                3 -> {
                    val angleDifference = abs(follower.pose.heading - dropAngle)
                    RobotLog.d("Angle difference: $angleDifference")
                    dashboard.telemetry.addData("Angle difference", angleDifference)
                    dashboard.telemetry.addData("Heading", follower.pose.heading)
                    if (angleDifference < 0.1 || pathTimer.elapsedTimeSeconds > 1.5) {
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
                        follower.turnDegrees(95.0, false)
                        state = 6
                    }
                }

                6 -> {
                    if (abs(follower.pose.heading - dropAngle) < 0.1 || pathTimer.elapsedTimeSeconds > 1.5) {
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
                        follower.setMaxPower(1.0)
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
                        follower.followPath(secondScore[0])
                        state = 92
                    }
                }

                92 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
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
                        follower.followPath(firstPickup[0])
                        state = 11
                    }
                }

                11 -> {
                    if (!follower.isBusy) { // Score first specimen
                        robot.claw.isClosed = true
                        state = 111
                    }
                }

                111 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[1])
                        state = 12
                    }
                }

                12 -> {
                    if (!follower.isBusy) {
                        robot.lift.targetPosition = Positions.Lift.down
                        robot.outtake.pendul.targetPosition = 0.7
                        robot.claw.isClosed = false
                        follower.followPath(firstPickup[1])
                        state = 13
                    }
                }

                13 -> {
                    if (!follower.isBusy) { // Score second specimen
                        robot.claw.isClosed = true
                        state = 131
                    }
                }

                131 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[2])
                        state = 14
                    }
                }

                14 -> {
                    if (!follower.isBusy) {
                        robot.lift.targetPosition = Positions.Lift.down
                        robot.outtake.pendul.targetPosition = 0.7
                        robot.claw.isClosed = false
                        follower.followPath(firstPickup[2])
                        state = 15
                    }
                }

                15 -> {
                    if (!follower.isBusy) { // Score third specimen
                        robot.claw.isClosed = true
                        state = 151
                    }
                }

                151 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(secondScore[3])
                        state = 16
                    }
                }

                16 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        robot.lift.targetPosition = Positions.Lift.down
                        follower.followPath(park)
                        state = 17
                    }
                }

                17 -> {
                    PositionStore.pose = follower.pose
                }

                -1 -> {
                    PositionStore.pose = follower.pose
                    robot.claw.isClosed = false
                    robot.lift.targetPosition = Positions.Lift.down
                }
            }

            robot.run(TelemetryPacket())
            Drawing.drawRobot(follower.pose, "#142780")
            follower.update()
            follower.drawOnDashBoard()
            dashboard.telemetry.update()
        }
    }
}
