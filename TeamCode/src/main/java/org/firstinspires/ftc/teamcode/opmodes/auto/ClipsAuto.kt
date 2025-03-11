package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.PathBuilder
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.PathFunction
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
        val hubList = hardwareMap.getAll(LynxModule::class.java)

        val robot = RobotHardware(hardwareMap)

        val dashboard = FtcDashboard.getInstance()
        val angle = Math.PI
        val beginPoint = Point(9.0, 60.5)
        val scorePoint =
            arrayOf(
                Point(35.0, 67.5),
                Point(35.0, 68.0),
                Point(35.0, 68.5),
                Point(35.0, 69.0),
                Point(35.0, 69.5),
            )
        val samplePoint =
            arrayOf(
                Point(57.7, 27.0),
                Point(57.7, 17.0),
                Point(57.7, 8.9),
            )
        val dropPoint =
            arrayOf(
                Point(23.0, samplePoint[0].y),
                Point(23.0, samplePoint[1].y),
                Point(17.0, samplePoint[2].y),
            )

        val scoreControl = Point(20.0, scorePoint[1].y)
        val pickupPoint = Point(17.0, 30.0)
        val parkPoint = Point(22.0, 50.0)

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
                    .addBezierCurve(dropPoint[2], scoreControl, scorePoint[1])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.lift.targetPosition = Positions.Lift.half
                        robot.claw.isClosed = true
                    }.addTemporalCallback(0.3) { robot.outtake.outtakePosition = OuttakePosition.BAR }
                    .build(),
            )

        scorePathList +=
            Array(3) {
                PathBuilder()
                    .addBezierLine(pickupPoint, scorePoint[it + 2])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.lift.targetPosition = Positions.Lift.half
                        robot.claw.isClosed = true
                    }.addTemporalCallback(0.1) { robot.outtake.outtakePosition = OuttakePosition.BAR }
                    .build()
            }

        val returnPath =
            Array(3) {
                PathBuilder()
                    .addBezierLine(scorePoint[it + 2], pickupPoint)
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                        robot.claw.isClosed = false
                    }.addTemporalCallback(0.3) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                        robot.lift.targetPosition = 0.0
                    }.build()
            }

        val scorePath = scorePathList.toTypedArray()

        val goToFirstSample: PathFunction = {
            addBezierCurve(
                scorePoint[0],
                Point(12.6, 67.0),
                Point(24.5, 9.0),
                Point(66.5, 51.0),
                samplePoint[0],
            )
            setConstantHeadingInterpolation(angle)
            addTemporalCallback(0.0) {
                robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                robot.claw.isClosed = false
            }
            addTemporalCallback(1.0) {
                robot.intake.targetPosition = IntakePositions.SPECIMEN_PICKUP
                robot.outtake.outtakePosition = OuttakePosition.PICKUP
                robot.lift.targetPosition = 0.0
            }
        }
        val dropFirstSample: PathFunction = {
            addBezierLine(samplePoint[0], dropPoint[0])
            setConstantHeadingInterpolation(angle)
        }
        val goToSecondSample: PathFunction = {
            addBezierCurve(dropPoint[0], Point(67.5, 28.5), samplePoint[1])
            setConstantHeadingInterpolation(angle)
        }
        val dropSecondSample: PathFunction = {
            addBezierLine(samplePoint[1], dropPoint[1])
            setConstantHeadingInterpolation(angle)
        }
        val goToThirdSample: PathFunction = {
            addBezierCurve(dropPoint[1], Point(65.0, 16.0), samplePoint[2])
            setConstantHeadingInterpolation(angle)
        }
        val dropThirdSample: PathFunction = {
            addBezierLine(samplePoint[2], dropPoint[2])
            setConstantHeadingInterpolation(angle)
        }

        val getSamples =
            PathBuilder()
                .goToFirstSample()
                .dropFirstSample()
                .goToSecondSample()
                .dropSecondSample()
                .goToThirdSample()
                .dropThirdSample()
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
        state = 0

        while (!isStopRequested) {
            when (state) {
                0 -> {
                    follower.followPath(scorePath[0])
                    state = 1
                }

                1 -> {
                    if (!follower.isBusy) {
                        follower.followPath(getSamples)
                        state = 2
                    }
                }

                2 -> {
                    if (!follower.isBusy) {
                        follower.followPath(scorePath[1])
                        state = 3
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
                        follower.followPath(scorePath[2])
                        state = 5
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
                        follower.followPath(scorePath[3])
                        state = 7
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
                        follower.followPath(scorePath[4])
                        state = 9
                    }
                }

                9 -> {
                    if (!follower.isBusy) {
                        follower.followPath(park)
                        state = -1
                    }
                }
            }
            follower.update()
            val packet = TelemetryPacket()
            robot.run(packet)
            dashboard.sendTelemetryPacket(packet)
        }
    }
}
