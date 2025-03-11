package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.follower.Follower
import com.pedropathing.follower.FollowerConstants
import com.pedropathing.localization.Pose
import com.pedropathing.util.PIDFController
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.RobotLog
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.Sensor
import org.firstinspires.ftc.teamcode.systems.subsystems.util.AllianceColor
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@TeleOp
open class MainTeleOp : LinearOpMode() {
    private lateinit var sensor: Sensor

    protected open val allianceColor = AllianceColor.RED

    protected var isYellowAllowed = false
    private val isAllianceColor get() =
        when (allianceColor) {
            AllianceColor.RED -> sensor.isRed
            AllianceColor.BLUE -> sensor.isBlue
        }
    private val isOppositeColor get() =
        when (allianceColor) {
            AllianceColor.RED -> sensor.isBlue
            AllianceColor.BLUE -> sensor.isRed
        }

    private val isRightColor get() = isAllianceColor || (isYellowAllowed && sensor.isYellow)
    private val isWrongColor get() = isOppositeColor || (!isYellowAllowed && sensor.isYellow)

    final override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)
        sensor = Sensor(hardwareMap)

        fun inTransfer() =
            robot.lift.targetPosition == Positions.Lift.transfer &&
                robot.outtake.outtakePosition == OuttakePosition.TRANSFER

        val headingPIDFCoefficients = FollowerConstants.headingPIDFCoefficients
        val headingPIDF = PIDFController(headingPIDFCoefficients)
        headingPIDF.targetPosition = Math.PI

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        val actionList =
            mutableListOf(
                robot.intake,
                robot.outtake,
                robot.extend,
                robot.lift,
                robot.claw,
            )

        val follower = Follower(this.hardwareMap)
        follower.setStartingPose(PositionStore.pose)

        var holdHeading = false

        val delayedActions = ActionList<FunctionAction<Unit>>()

        fun finishTransfer() {
            robot.claw.isClosed = true
            delayedActions.add(DelayedAction(250.milliseconds) { robot.intake.isClosed = false })
        }

        val pressActionList =
            ActionList(
                FunctionAction(controlGamepad::right_bumper) {
                    if (inTransfer()) {
                        if (robot.claw.isClosed) {
                            robot.intake.claw.isClosed = true
                            delayedActions.add(
                                DelayedAction(
                                    250.milliseconds,
                                ) {
                                    robot.claw.isClosed = false
                                },
                            )
                        } else {
                            finishTransfer()
                        }
                    } else if (robot.claw.isClosed &&
                        robot.lift.targetPosition == Positions.Lift.half &&
                        robot.outtake.outtakePosition == OuttakePosition.BAR
                    ) {
                        robot.claw.isClosed = false
                        robot.outtake.pendul.targetPosition = Positions.Pendul.specimenRelease
                    } else {
                        robot.claw.isClosed = !robot.claw.isClosed
                    }
                },
                FunctionAction(controlGamepad::left_bumper) {
                    robot.intake.targetPosition = IntakePositions.TRANSFER
                },
                FunctionAction(controlGamepad::cross) {
                    robot.intake.targetPosition = IntakePositions.PICKUP
                },
                FunctionAction(moveGamepad::cross) {
                    if (!robot.intake.isExtended) {
                        holdHeading = !holdHeading
                    }
                },
                FunctionAction(controlGamepad::right_stick_button) {
                    robot.lift.targetPosition = Positions.Lift.up
                    robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                },
                FunctionAction(controlGamepad::left_stick_button) {
                    robot.lift.targetPosition = Positions.Lift.hang
                },
                FunctionAction(controlGamepad::circle) {
                    if (inTransfer()) {
                        if (!robot.claw.isClosed) {
                            finishTransfer()
                            delayedActions.add(
                                DelayedAction(0.3.seconds) {
                                    robot.lift.targetPosition = Positions.Lift.up
                                    robot.outtake.outtakePosition = OuttakePosition.BASKET
                                },
                            )
                        } else {
                            robot.lift.targetPosition = Positions.Lift.up
                            robot.outtake.outtakePosition = OuttakePosition.BASKET
                        }
                    } else {
                        robot.lift.targetPosition = Positions.Lift.up
                    }
                },
                FunctionAction({ -controlGamepad.left_stick_y < -0.7 }) {
                    // Joystick-urile sunt inversate
                    robot.lift.targetPosition = Positions.Lift.half
                    robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                    robot.claw.isClosed = false
                },
                FunctionAction(controlGamepad::dpad_right) {
                    robot.claw.isClosed = false
                    holdHeading = false
                    val setOuttakeLiftPosition = {
                        robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                        robot.lift.targetPosition = Positions.Lift.transfer
                    }
                    if (robot.intake.targetPosition != IntakePositions.TRANSFER) {
                        robot.intake.targetPosition = IntakePositions.TRANSFER
                        delayedActions.add(DelayedAction(0.2.seconds) { setOuttakeLiftPosition() })
                    } else {
                        setOuttakeLiftPosition()
                    }
                },
                FunctionAction({ -controlGamepad.right_stick_y < -0.9 && !robot.lift.isResetting }) {
                    robot.lift.isResetting = !robot.lift.isResetting
                },
                FunctionAction({ -controlGamepad.right_stick_y >= -0.9 && robot.lift.isResetting }) {
                    robot.lift.isResetting = !robot.lift.isResetting
                },
                FunctionAction(moveGamepad::right_bumper) {
                    delayedActions.add(
                        FunctionAction(robot.intake::isUp, willCancel = true) {
                            var colorDetections = 0
                            colorDetections += if (sensor.isBlue) 1 else 0
                            colorDetections += if (sensor.isRed) 1 else 0
                            colorDetections += if (sensor.isYellow) 1 else 0

                            if (colorDetections > 1) {
                                RobotLog.e(
                                    """
                                    Multiple Colors Detected:
                                        Red: ${sensor.isRed},
                                        Blue: ${sensor.isBlue},
                                        Yellow: ${sensor.isYellow},
                                    """.trimIndent(),
                                )
                            }

                            if (!controlGamepad.cross && !controlGamepad.left_bumper) {
                                if (!sensor.isHoldingSample || isWrongColor) {
                                    robot.intake.isClosed = false
                                    RobotLog.d("Reached with no specimen")
                                } else if (sensor.isHoldingSample && isRightColor) {
                                    robot.intake.targetPosition = IntakePositions.TRANSFER
                                }
                            }
                        },
                    )
                },
                FunctionAction(moveGamepad::square) {
                    isYellowAllowed = !isYellowAllowed
                    controlGamepad.rumbleBlips(if (isYellowAllowed) 2 else 1)
                },
            )

        waitForStart()

        dashboard.clearTelemetry()
        follower.startTeleopDrive()

        robot.intake.targetPosition = IntakePositions.TRANSFER
        robot.extend.targetPosition = Positions.Extend.`in`
        robot.outtake.pendul.targetPosition = 0.8
        robot.intake.isClosed = false

        while (opModeIsActive()) {
            val powerMultiply = if (robot.intake.targetPosition == IntakePositions.PICKUP) 0.4 else 1.0

            // Movement
            follower.setTeleOpMovementVectors(
                -moveGamepad.left_stick_y.toDouble(),
                -moveGamepad.left_stick_x.toDouble() * powerMultiply,
                if (holdHeading) {
                    follower.headingOffset += moveGamepad.right_stick_x.toDouble() * 0.025

                    headingPIDF.updatePosition(follower.pose.heading)
                    headingPIDF.runPIDF()
                } else {
                    -moveGamepad.right_stick_x.toDouble() * powerMultiply
                },
                false,
            )
            follower.update()
            follower.drawOnDashBoard()

            // Ordinea e intentionata, ultima din pressActionList pica daca e invers
            delayedActions()
            pressActionList()

            robot.applyPositions(controlGamepad)

            val telemetryPacket = TelemetryPacket()
            telemetryPacket.put("Robot Pose", follower.pose.asPedroCoordinates)
            runActions(actionList, telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            if (moveGamepad.left_stick_button) {
                follower.pose = Pose(0.0, 0.0, 0.0)
            }

            if (moveGamepad.right_stick_button) {
                follower.pose = Pose(0.0, 0.0, Math.PI)
            }

            if (robot.intake.targetPosition in Intake.pickupPositions) {
                when {
                    gamepad1.right_bumper -> {
                        robot.intake.pickUp()
                    }

                    gamepad1.left_bumper -> {
                        robot.intake.isClosed = false
                        robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.middle
                    }
                }

                if (moveGamepad.right_trigger > 0.7) {
                    robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.right
                    robot.intake.claw.isClosed = false
                }

                if (moveGamepad.left_trigger > 0.7) {
                    robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.left
                    robot.intake.claw.isClosed = false
                }
            }
        }
    }

    /**
     * Runs all Actions in [actionList], deleting every finished [Action]
     */
    private fun runActions(
        actionList: MutableList<Action>,
        telemetryPacket: TelemetryPacket,
    ) {
        val iterator = actionList.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (!action.run(telemetryPacket)) iterator.remove()
        }
    }

    /**
     * Apply positions to the robot hardware based on the gamepad input
     *
     * @throws RuntimeException if any of the target positions is not implemented
     * (workaround for DriverStation not displaying NotImplementedException)
     */
    private fun RobotHardware.applyPositions(gamepad: Gamepad) {
        try {
            lift.targetPosition =
                when {
                    gamepad.square -> Positions.Lift.down
                    gamepad.triangle -> Positions.Lift.half
                    else -> lift.targetPosition
                }

            if (gamepad.dpad_down) {
                intake.targetPosition = IntakePositions.SPECIMEN_PICKUP
            }

            val outtakePosition =
                when {
                    gamepad.dpad_up -> OuttakePosition.BASKET
                    gamepad.dpad_left -> OuttakePosition.BAR
                    gamepad.dpad_down -> OuttakePosition.PICKUP
                    else -> null
                }
            if (outtakePosition != null) {
                outtake.outtakePosition = outtakePosition
            }
        } catch (e: NotImplementedError) {
            // Driver station nu arata NotImplementedError, doar oprește OpMode
            throw RuntimeException(e)
        }
    }
}
