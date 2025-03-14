package org.firstinspires.ftc.teamcode.opmodes.auto

import android.util.Log
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
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.ActionList
import org.firstinspires.ftc.teamcode.util.DelayedAction
import org.firstinspires.ftc.teamcode.util.FunctionAction
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Autonomous
class Basket : LinearOpMode() {
    val beginPose = Pose(8.7, 105.0, Math.toRadians(-90.0))
    val samplePoses =
        arrayOf(
            Pose(24.35, 126.86, Math.toRadians(-19.0)),
            Pose(21.5, 128.6, Math.toRadians(0.0)),
            Pose(23.2, 131.6, Math.toRadians(18.5)),
        )
    val scorePose = Pose(20.0, 125.5, Math.toRadians(-45.0))
    val scoreAngle = Math.toRadians(-45.0)
    val parkPose = Pose(64.2, 92.3, Math.toRadians(90.0))
    val parkControl = Point(65.5, 130.1)

    private val pathTimer = Timer()
    private var state = 0
        set(value) {
            field = value
            pathTimer.resetTimer()
        }

    val delayedActions = ActionList<FunctionAction<Unit>, Unit>()

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

        val firstScore =
            PathBuilder()
                .addBezierCurve(Point(beginPose), Point(scorePose))
                .setLinearHeadingInterpolation(beginPose.heading, scorePose.heading)
                .build()

        val pickupSamples =
            arrayOf(
                PathBuilder()
                    .addBezierLine(Point(scorePose), Point(samplePoses[0]))
                    .setLinearHeadingInterpolation(scoreAngle, samplePoses[0].heading, 0.6)
                    .build(),
                PathBuilder()
                    .addBezierLine(Point(scorePose), Point(samplePoses[1]))
                    .setLinearHeadingInterpolation(scoreAngle, samplePoses[1].heading, 0.6)
                    .build(),
                PathBuilder()
                    .addBezierLine(Point(scorePose), Point(samplePoses[2]))
                    .setLinearHeadingInterpolation(scoreAngle, samplePoses[2].heading, 0.6)
                    .build(),
            )

        val scorePaths =
            arrayOf(
                PathBuilder()
                    .addBezierLine(Point(samplePoses[0]), Point(scorePose))
                    .setLinearHeadingInterpolation(samplePoses[0].heading, scorePose.heading)
                    .build(),
                PathBuilder()
                    .addBezierLine(Point(samplePoses[1]), Point(scorePose))
                    .setLinearHeadingInterpolation(samplePoses[1].heading, scorePose.heading)
                    .build(),
                PathBuilder()
                    .addBezierLine(Point(samplePoses[2]), Point(scorePose))
                    .setLinearHeadingInterpolation(samplePoses[2].heading, scorePose.heading)
                    .build(),
            )

        val parkPath =
            PathBuilder()
                .addBezierCurve(Point(scorePose), parkControl, Point(parkPose))
                .setLinearHeadingInterpolation(scorePose.heading, parkPose.heading, 0.5)
                .addParametricCallback(0.9) {
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                }.setZeroPowerAccelerationMultiplier(2.0)
                .build()

        val transfer: (Int) -> Unit = {
            robot.lift.targetPosition = Positions.Lift.hang
            robot.outtake.outtakePosition = OuttakePosition.TRANSFER
            robot.intake.targetPosition = IntakePositions.TRANSFER

            delayedActions +=
                DelayedAction(200.0.milliseconds) {
                    robot.lift.targetPosition = Positions.Lift.transfer
                    delayedActions +=
                        FunctionAction(robot.lift::atTarget, willCancel = true) {
                            robot.claw.isClosed = true
                            delayedActions +=
                                DelayedAction(100.0.milliseconds) {
                                    robot.intake.claw.isClosed = false
                                }
                            delayedActions +=
                                DelayedAction(150.0.milliseconds) {
                                    robot.lift.targetPosition = Positions.Lift.up
                                }
                            delayedActions +=
                                DelayedAction(1.5.seconds) {
                                    robot.outtake.outtakePosition = OuttakePosition.BASKET
                                    state = it
                                }
                        }
                }

            state = -10
        }
        intake.targetPosition = IntakePositions.SPECIMEN_PICKUP
        pendul.targetPosition = Positions.Pendul.transfer

        while (opModeInInit()) {
            val packet = TelemetryPacket()
            intake.run(packet)
            pendul.run(packet)
            dashboard.sendTelemetryPacket(packet)
        }
        val pickupDelay = 0.3
        val dropDelay = 0.5.seconds

        follower.poseUpdater.resetIMU()
        follower.pose = beginPose

        val opModeTimer = Timer()

        while (!isStopRequested) {
            if (opModeTimer.elapsedTimeSeconds > 29.9) {
                state = -1
            }

            when (state) {
                -10 -> {
                    // Reserved for idling while waiting for a delayedAction to finish
                    try {
                        assert(delayedActions.isNotEmpty())
                    } catch (e: AssertionError) {
                        throw RuntimeException(e)
                    }
                }

                0 -> {
//                    follower.followPath(firstScore)
                    robot.outtake.outtakePosition = OuttakePosition.BASKET
                    robot.lift.targetPosition = Positions.Lift.up
                    delayedActions +=
                        DelayedAction(1.5.seconds) {
                            follower.followPath(firstScore)
                            state = 1
                        }
                    state = -10
                }

                1 -> {
                    if (!follower.isBusy) {
                        robot.outtake.outtakePosition = OuttakePosition.BASKET
                        delayedActions +=
                            DelayedAction(0.5.seconds) {
                                robot.claw.isClosed = false
                                state = 2
                            }
                        state = -10
                    }
                }

                2 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(pickupSamples[0])
                        robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.middle - 0.12
                        robot.intake.targetPosition = IntakePositions.PICKUP
                        robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                        robot.lift.targetPosition = Positions.Lift.hang
                        state = 3
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
                        transfer(41)
                    }
                }

                41 -> {
                    follower.followPath(scorePaths[0])
                    state = 5
                }

                5 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        Log.d("ActionList", "${delayedActions.size}")
                        state = -10
                        delayedActions +=
                            DelayedAction(dropDelay) {
                                state = 6
                            }
                    }
                }

                6 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(pickupSamples[1])
                        robot.intake.targetPosition = IntakePositions.PICKUP
                        robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                        robot.lift.targetPosition = Positions.Lift.hang
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
                    if (pathTimer.elapsedTimeSeconds > pickupDelay) {
                        transfer(81)
                    }
                }

                81 -> {
                    follower.followPath(scorePaths[1])
                    state = 9
                }

                9 -> {
                    if (!follower.isBusy && delayedActions.isEmpty()) {
                        robot.claw.isClosed = false
                        state = -10
                        delayedActions +=
                            DelayedAction(dropDelay) {
                                state = 10
                            }
                    }
                }

                10 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.2) {
                        follower.followPath(pickupSamples[2])
                        robot.intake.targetPosition = IntakePositions.PICKUP
                        robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                        robot.lift.targetPosition = Positions.Lift.hang
                        state = 11
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
                        transfer(121)
                    }
                }

                121 -> {
                    follower.followPath(scorePaths[2])
                    state = 13
                }

                13 -> {
                    if (!follower.isBusy && delayedActions.isEmpty()) {
                        robot.claw.isClosed = false
                        state = -10
                        delayedActions +=
                            DelayedAction(dropDelay) {
                                state = 14
                            }
                    }
                }

                14 -> {
                    follower.followPath(parkPath)
                    robot.lift.targetPosition = Positions.Lift.half + 100.0
                    robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                    robot.intake.targetPosition = IntakePositions.TRANSFER
                    state = -1
                }
            }

            robot.run(TelemetryPacket())
            Drawing.drawDebug(follower)
            follower.update()
            follower.drawOnDashBoard()
            dashboard.telemetry.update()
            delayedActions()
        }
    }
}
