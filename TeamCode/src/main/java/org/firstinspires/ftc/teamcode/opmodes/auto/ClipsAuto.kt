package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.*
import com.pedropathing.util.Constants
import com.pedropathing.util.Drawing
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.*
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants
import kotlin.math.PI
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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
        val angle = PI
        val beginPoint = Point(8.0, 60.5)
        val scorePoint =
            arrayOf(
                Point(38.0, 72.0),
                Point(38.0, 70.0),
                Point(38.0, 69.0),
                Point(38.0, 69.0),
                Point(38.0, 69.0),
            )
        val samplePoint =
            arrayOf(
                Point(57.7, 27.0),
                Point(57.7, 17.0),
                Point(57.7, 10.0),
            )

        val scoreControl = Point(7.0, scorePoint[1].y)
        val pickupPoint = Point(9.8, 30.0)
        val parkPoint = Point(22.0, 50.0)

        val dropPoint =
            arrayOf(
                Point(30.0, samplePoint[0].y),
                Point(30.0, samplePoint[1].y),
                Point(18.0, samplePoint[2].y),
            )

        val smallScoreControl = Point(25.0, 64.0)

        val scorePathList =
            mutableListOf(
                PathBuilder()
                    .addBezierLine(beginPoint, scorePoint[0])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.lift.targetPosition = Positions.Lift.half
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                    }.build(),
                PathBuilder()
                    .addBezierCurve(pickupPoint, scoreControl, scorePoint[1])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.lift.targetPosition = Positions.Lift.half
                        robot.claw.isClosed = true
                    }.addTemporalCallback(0.1) { robot.outtake.outtakePosition = OuttakePosition.BAR }
                    .build(),
            )

        scorePathList +=
            Array(3) {
                PathBuilder()
                    .addBezierCurve(pickupPoint, smallScoreControl, scorePoint[it + 2])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.lift.targetPosition = Positions.Lift.half
                        robot.claw.isClosed = true
                        follower.setMaxPower(1.0)
                    }.addTemporalCallback(0.1) { robot.outtake.outtakePosition = OuttakePosition.BAR }
                    .build()
            }

        val returnPath =
            Array(3) {
                PathBuilder()
                    .addBezierCurve(scorePoint[it + 2], Point(24.0, 69.0), Point(24.0, 30.0), pickupPoint)
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                        robot.claw.isClosed = false
                    }.addTemporalCallback(0.1) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                        robot.lift.targetPosition = 0.0
                    }.setZeroPowerAccelerationMultiplier(1.2)
                    .build()
            }

        val scorePath = scorePathList.toTypedArray()

        val goToFirstSample: PathFunction = {
            addBezierCurve(
                scorePoint[0],
                Point(13.0, 60.0),
                Point(23.0, 7.0),
                Point(60.0, 51.5),
                Point(64.5, 32.5),
                samplePoint[0],
            )
            setConstantHeadingInterpolation(angle)
            addTemporalCallback(0.0) {
                robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                robot.claw.isClosed = false
            }
            addParametricCallback(0.4) {
                robot.intake.targetPosition = IntakePositions.SPECIMEN_PICKUP
                robot.outtake.outtakePosition = OuttakePosition.PICKUP
                robot.lift.targetPosition = 0.0
                follower.setMaxPower(0.5)
            }
            addParametricCallback(0.9) {
                follower.setMaxPower(1.0)
            }
        }
        val dropFirstSample: PathFunction = {
            addBezierLine(samplePoint[0], dropPoint[0])
            setConstantHeadingInterpolation(angle)
        }
        val goToSecondSample: PathFunction = {
            addBezierCurve(dropPoint[0], Point(67.0, 26.0), samplePoint[1])
            addParametricCallback(0.3) { follower.setMaxPower(0.6) }
            addParametricCallback(0.9) { follower.setMaxPower(1.0) }
            setConstantHeadingInterpolation(angle)
        }
        val dropSecondSample: PathFunction = {
            addBezierLine(samplePoint[1], dropPoint[1])
            setConstantHeadingInterpolation(angle)
        }
        val goToThirdSample: PathFunction = {
            addBezierCurve(dropPoint[1], Point(65.0, 16.0), samplePoint[2])
            addParametricCallback(0.3) { follower.setMaxPower(0.6) }
            addParametricCallback(0.9) { follower.setMaxPower(1.0) }
            setConstantHeadingInterpolation(angle)
        }
        val dropThirdSample: PathFunction = {
            addBezierLine(samplePoint[2], dropPoint[2])
            setConstantHeadingInterpolation(angle)
        }

        val pickupFirstSpecimen =
            Path(BezierCurve(dropPoint[2], Point(dropPoint[2].x, pickupPoint.y), pickupPoint))
        pickupFirstSpecimen.setConstantHeadingInterpolation(PI)
        pickupFirstSpecimen.zeroPowerAccelerationMultiplier = 1.7

        val getSamples =
            PathBuilder()
                .goToFirstSample()
                .dropFirstSample()
                .goToSecondSample()
                .dropSecondSample()
                .goToThirdSample()
                .dropThirdSample()
                .setZeroPowerAccelerationMultiplier(8.0)
                .build()

        val park =
            PathBuilder()
                .addBezierLine(scorePoint[4], parkPoint)
                .setTangentHeadingInterpolation()
                .addTemporalCallback(0.0) {
                    robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                    robot.claw.isClosed = false
                }.addTemporalCallback(0.4) {
                    robot.intake.targetPosition = IntakePositions.PICKUP
                }.build()

        robot.outtake.pendul.targetPosition = 0.95
        robot.intake.targetPosition = IntakePositions.SPECIMEN_PICKUP

        while (opModeInInit()) {
            val p = TelemetryPacket()
            robot.intake.run(p)
            robot.outtake.pendul.run(p)
            dashboard.sendTelemetryPacket(p)
        }

        follower.poseUpdater.resetIMU()
        follower.pose = Pose(beginPoint.x, beginPoint.y, angle)
        val actions = ActionList<FunctionAction<Int>, Int> { state = this }

        val clawDelay = 0.40.milliseconds
//        robot.outtake.pendul.useServoSmooth = false

        while (!isStopRequested) {
            when (state) {
                -10 -> {
                    require(actions.isNotEmpty())
                }

                0 -> {
                    robot.outtake.outtakePosition = OuttakePosition.BAR
                    robot.lift.targetPosition = Positions.Lift.half
                    state = -10
                    actions +=
                        DelayedAction(0.15.seconds) {
                            follower.followPath(scorePath[0])
                            1
                        }
                }

                1 -> {
                    if (!follower.isBusy) {
                        follower.followPath(getSamples, false)
                        state = 2
                    }
                }

                2 -> {
                    if (!follower.isBusy) {
                        follower.followPath(pickupFirstSpecimen, true)
                        follower.setMaxPower(0.85)
                        state = 21
                    }
                }

                21 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        follower.setMaxPower(1.0)
                        actions +=
                            DelayedAction(clawDelay) {
                                follower.followPath(scorePath[1])
                                3
                            }
                        state = -10
                    }
                }

                3 -> {
                    if (!follower.isBusy) {
                        follower.followPath(returnPath[0])
                        state = 4
                    }
                }

                4 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        actions +=
                            DelayedAction(clawDelay) {
                                follower.followPath(scorePath[2])
                                5
                            }
                        state = -10
                    }
                }

                5 -> {
                    if (!follower.isBusy) {
                        follower.followPath(returnPath[1])
                        state = 6
                    }
                }

                6 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        actions +=
                            DelayedAction(clawDelay) {
                                follower.followPath(scorePath[3])
                                7
                            }
                        state = -10
                    }
                }

                7 -> {
                    if (!follower.isBusy) {
                        follower.followPath(returnPath[2])
                        state = 8
                    }
                }

                8 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        actions +=
                            DelayedAction(clawDelay) {
                                follower.followPath(scorePath[4])
                                9
                            }
                        state = -10
                    }
                }

                9 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                        actions +=
                            DelayedAction(80.0.milliseconds) {
                                follower.followPath(park)
                                -1
                            }
                        state = -10
                    }
                }
            }
            follower.update()
            val packet = TelemetryPacket()
            robot.run(packet)
            dashboard.sendTelemetryPacket(packet)
            actions()
            Drawing.drawDebug(follower)
            autoPose = follower.pose
        }
    }
}
