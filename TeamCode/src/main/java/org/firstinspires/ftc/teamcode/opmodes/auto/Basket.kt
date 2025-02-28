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
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.DelayedOpMode
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@Autonomous
class Basket : DelayedOpMode() {
    val beginPose = Pose(11.6, 118.0, Math.toRadians(45.0))
    val samplePoints = arrayOf(
        Point(21.5, 126.3),
        Point(21.0, 128.6),
        Point(23.2, 131.6)
    )
    val scorePose = Pose(16.0, 128.0, Math.toRadians(-45.0))
    val parkPose = Pose(64.2, 92.3, Math.toRadians(90.0))
    val parkControl = Point(65.5, 130.1)

    private val pathTimer = Timer()
    private var state = 0
        set(value) {
            field = value
            pathTimer.resetTimer()
        }

    override val delayedActions: MutableList<Pair<Long, Runnable>> = mutableListOf()

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

        val firstScore = PathBuilder()
            .addBezierLine(Point(beginPose), Point(scorePose))
            .setLinearHeadingInterpolation(beginPose.heading, scorePose.heading)
            .build()

        val pickupSamples = arrayOf(
            PathBuilder().addBezierLine(Point(scorePose), samplePoints[0])
                .setTangentHeadingInterpolation()
                .build(),
            PathBuilder().addBezierLine(Point(scorePose), samplePoints[1])
                .setTangentHeadingInterpolation()
                .build(),
            PathBuilder().addBezierLine(Point(scorePose), samplePoints[2])
                .setTangentHeadingInterpolation()
                .build()
        )

        val scorePaths = arrayOf(
            PathBuilder().addBezierLine(samplePoints[0], Point(scorePose))
                .setTangentHeadingInterpolation()
                .build(),
            PathBuilder().addBezierLine(samplePoints[1], Point(scorePose))
                .setTangentHeadingInterpolation()
                .build(),
            PathBuilder().addBezierLine(samplePoints[2], Point(scorePose))
                .setTangentHeadingInterpolation()
                .build()
        )

        val parkPath = PathBuilder().addBezierCurve(Point(scorePose), parkControl, Point(parkPose))
            .setLinearHeadingInterpolation(scorePose.heading, parkPose.heading)
            .build()

        while (opModeInInit()) {
            val packet = TelemetryPacket()
            intake.run(packet)
            pendul.run(packet)
            dashboard.sendTelemetryPacket(packet)
        }
        val pickupDelay = 0.12

        val opModeTimer = Timer()

        while (!isStopRequested) {
            if (opModeTimer.elapsedTimeSeconds > 29.9) {
                state = -1
            }

            when (state) {
                0 -> {
                    follower.followPath(firstScore)
                    robot.outtake.outtakePosition = OuttakePosition.BASKET
                }
                1 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        state = 2
                    }
                }
                2 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(pickupSamples[0])
                        robot.intake.targetPosition = IntakePositions.PICKUP
                    }
                }
                3 -> {
                    if (!follower.isBusy) {
                        robot.intake.pickUp()
                        state = 4
                    }
                }
                4 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        follower.followPath(scorePaths[0])
                        transfer(robot)
                        delayedActions.addDelayed(0.8) {
                            robot.lift.targetPosition = Positions.Lift.up
                            robot.outtake.outtakePosition = OuttakePosition.BASKET
                        }
                    }
                }
                5 -> {
                    if (!follower.isBusy && delayedActions.isEmpty()) {
                        robot.claw.isClosed = false
                        state = 6
                    }
                }
                6 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(pickupSamples[1])
                        robot.intake.targetPosition = IntakePositions.PICKUP
                    }
                }
                7 -> {
                    if (!follower.isBusy) {
                        robot.intake.pickUp()
                        state = 8
                    }
                }
                8 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        follower.followPath(scorePaths[1])
                        transfer(robot)
                        delayedActions.addDelayed(0.8) {
                            robot.lift.targetPosition = Positions.Lift.up
                            robot.outtake.outtakePosition = OuttakePosition.BASKET
                        }
                    }
                }
                9 -> {
                    if (!follower.isBusy && delayedActions.isEmpty()) {
                        robot.claw.isClosed = false
                        state = 10
                    }
                }
                10 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(pickupSamples[2])
                        robot.intake.targetPosition = IntakePositions.PICKUP
                    }
                }
                11 -> {
                    if (!follower.isBusy) {
                        robot.intake.pickUp()
                        state = 12
                    }
                }
                12 -> {
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        follower.followPath(scorePaths[2])
                        transfer(robot)
                        delayedActions.addDelayed(0.8) {
                            robot.lift.targetPosition = Positions.Lift.up
                            robot.outtake.outtakePosition = OuttakePosition.BASKET
                        }
                    }
                }
                13 -> {
                    if (!follower.isBusy && delayedActions.isEmpty()) {
                        robot.claw.isClosed = false
                        state = 14
                    }
                }
                14 -> {
                    follower.followPath(parkPath)
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                    robot.intake.targetPosition = IntakePositions.TRANSFER
                }
            }

            robot.run(TelemetryPacket())
            Drawing.drawRobot(follower.pose, "#142780")
            follower.update()
            follower.drawOnDashBoard()
            dashboard.telemetry.update()
            delayedActions.run()
        }
    }
}
