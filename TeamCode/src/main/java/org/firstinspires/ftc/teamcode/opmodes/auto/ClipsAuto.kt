package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.*
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.PositionStore
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
        val firstScorePose = Pose(40.0, 68.7, Math.toRadians(180.001))

        val beginPose = Pose(10.0, 60.0, Math.toRadians(180.0))
        val scorePose = Pose(40.0, 73.0, Math.toRadians(180.001))
        val pickup1Intermediary = Pose(36.0, 36.0, 0.0)
        val pickup1Pose = Pose(60.3, 29.06, 0.0)
        val drop1Pose = Pose(32.0, 28.0, 0.0)
        val pickup2Pose = Pose(60.3, 20.5, 0.0)
        val pickup2Intermediary = Point(62.3, 30.0)
        val drop2Pose = Pose(32.0, 20.5, 0.0)
        val pickup3Pose = Pose(60.3, 13.0, 0.0)
        val pickup3Intermediary = Point(62.3, 20.0)
        val drop3Pose = Pose(18.0, 13.0, 0.0)

        val dropControl = Point(30.0, 24.0)
        val humanPickup = Pose(14.0, 20.0, 0.0)

        val scorePreloadPath = Path(BezierLine(Point(beginPose), Point(firstScorePose)))
        scorePreloadPath.setConstantHeadingInterpolation(Math.toRadians(180.0))
        scorePreloadPath.zeroPowerAccelerationMultiplier = 3.7

        scorePose.y += 2.5
        scorePose.x += 1.5

        val humanPickupPath = Path(BezierCurve(Point(drop3Pose), dropControl, Point(humanPickup)))
        humanPickupPath.setConstantHeadingInterpolation(0.0)
        humanPickupPath.zeroPowerAccelerationMultiplier = 2.5

        humanPickup.x -= 2.4

        val scorePaths =
            Array(4) {
                val newScorePose = scorePose.copy()
                newScorePose.y += (1.5) * it

                val scoreControl = Point(humanPickup.x, newScorePose.y)
                val scoreControl2 = Point(25.0, newScorePose.y)

                val goPath =
                    follower
                        .pathBuilder()
                        .addPath(
                            BezierCurve(Point(humanPickup), scoreControl, scoreControl2)
                        ).setLinearHeadingInterpolation(humanPickup.heading, newScorePose.heading)
                        .addPath(
                            BezierLine(scoreControl2, Point(newScorePose))
                        ).setConstantHeadingInterpolation(newScorePose.heading)
                        .build()

                val returnPath =
                    follower
                        .pathBuilder()
                        .addPath(
                            BezierCurve(
                                Point(newScorePose),
                                Point(20.0, 70.0),
                                Point(48.0, 24.0),
                                Point(humanPickup)
                            )
                        ).setLinearHeadingInterpolation(newScorePose.heading, humanPickup.heading)
                        .setZeroPowerAccelerationMultiplier(2.0)
                        .build()

                Pair(goPath, returnPath)
            }

        scorePose.x -= 1.5

        val drop1Path =
            follower
                .pathBuilder()
                .addPath(
                    BezierCurve(Point(firstScorePose), Point(14.0, 44.5), Point(pickup1Intermediary))
                ).setLinearHeadingInterpolation(Math.toRadians(180.001), 0.0)
                .addPath(BezierCurve(Point(pickup1Intermediary), Point(62.6, 38.8), Point(pickup1Pose)))
                .setConstantHeadingInterpolation(0.0)
                .addPath(BezierLine(Point(pickup1Pose), Point(drop1Pose)))
                .setConstantHeadingInterpolation(0.0)

        val drop2Path =
            drop1Path
                .addPath(BezierCurve(Point(drop1Pose), pickup2Intermediary, Point(pickup2Pose)))
                .setConstantHeadingInterpolation(0.0)
                .addPath(BezierLine(Point(pickup2Pose), Point(drop2Pose)))
                .setConstantHeadingInterpolation(0.0)

        val drop3Path =
            drop2Path
                .addPath(BezierCurve(Point(drop2Pose), pickup3Intermediary, Point(pickup3Pose)))
                .setConstantHeadingInterpolation(0.0)
                .addPath(BezierLine(Point(pickup3Pose), Point(drop3Pose)))
                .setConstantHeadingInterpolation(0.0)

        val dropPaths = drop3Path.build()

        follower.setStartingPose(beginPose)

        val endPath = BezierCurve(Point(scorePose), Point(25.0, scorePose.y), Point(humanPickup))

        waitForStart()

        state = 0

        while (!isStopRequested) {
            val p = TelemetryPacket()
            robot.run(p)
            dashboard.sendTelemetryPacket(p)
            dashboard.telemetry.update()

            when (state) {
                0 -> {
                    robot.lift.targetPosition = Positions.Lift.half
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

                        follower.followPath(dropPaths, false)
                        state = 1
                    }
                }

                1 -> {
                    if (pathTimer.elapsedTimeSeconds >= 1.0) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }

                    if (!follower.isBusy) {
                        follower.followPath(humanPickupPath)
                        state = 3
                    }
                }

                3 -> {
                    if (!follower.isBusy) {
                        robot.claw.isClosed = true

                        state = 4
                    }
                }

                4 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.05) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                        robot.lift.targetPosition = Positions.Lift.half
                    }
                    if (pathTimer.elapsedTimeSeconds > 0.15) {
                        follower.headingOffset = Math.toRadians(-10.0)
                        follower.followPath(scorePaths[0].first)
                        follower.xOffset = -3.3
                        state = 5
                    }
                }

                5 -> {
                    if (!follower.isBusy || pathTimer.elapsedTimeSeconds > 3.8) {
                        follower.resetOffset()
                        follower.pose = scorePose
//                        follower.xOffset = 0.6
                        robot.claw.isClosed = false
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                        follower.followPath(scorePaths[0].second)
                        state = 6
                    }
                }

                6 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.5) {
                        robot.lift.targetPosition = Positions.Lift.down
                    }

                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(scorePaths[1].first)
                        state = 7
                    }
                }

                7 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                        follower.headingOffset = Math.toRadians(-10.0)
                    }

                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        follower.followPath(Path(endPath))
                        robot.lift.targetPosition = Positions.Lift.down
                        state = 8
                    }
                }

                /*

                8 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }

                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(scorePaths[2].first)
                        state = 9
                    }
                }

                9 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                        follower.headingOffset = Math.toRadians(-10.0)
                    }

                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        follower.followPath(scorePaths[2].second)
                        robot.lift.targetPosition = Positions.Lift.down
                        state = 10
                    }
                }

                10 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.PICKUP
                    }

                    if (!follower.isBusy) {
                        robot.claw.isClosed = true
                        robot.lift.targetPosition = Positions.Lift.half
                        follower.followPath(scorePaths[3].first)
                        state = 11
                    }
                }

                11 -> {
                    if (pathTimer.elapsedTimeSeconds > 0.5) {
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                        follower.headingOffset = Math.toRadians(-10.0)
                    }

                    if (!follower.isBusy) {
                        robot.claw.isClosed = false
                        follower.followPath(scorePaths[3].second)
                        robot.lift.targetPosition = Positions.Lift.down
                        state = 12
                    }
                }
                 */
            }

//            follower.setMaxPower(11.5 / hubList.map { it.getInputVoltage(VoltageUnit.VOLTS) }.average())
            follower.update()
            follower.drawOnDashBoard()
        }

        PositionStore.pose = follower.pose
    }
}
