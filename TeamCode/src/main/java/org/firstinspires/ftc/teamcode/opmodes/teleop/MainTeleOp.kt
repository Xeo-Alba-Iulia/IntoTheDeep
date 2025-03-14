package org.firstinspires.ftc.teamcode.opmodes.teleop

import android.util.Log
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

    private var isRed = false
    private var isBlue = false
    private var isYellow = false

    protected var isYellowAllowed = false
    private val isAllianceColor get() =
        when (allianceColor) {
            AllianceColor.RED -> isRed
            AllianceColor.BLUE -> isBlue
        }
    private val isOppositeColor get() =
        when (allianceColor) {
            AllianceColor.RED -> isBlue
            AllianceColor.BLUE -> isRed
        }

    private fun Boolean.toInt() = if (this) 1 else 0

    private val isRightColor get() = isAllianceColor || (isYellowAllowed && isYellow)
    private val isWrongColor get() = isOppositeColor || (!isYellowAllowed && isYellow)

    final override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)
        sensor = Sensor(hardwareMap)

        fun inTransfer() =
            robot.lift.targetPosition == Positions.Lift.transfer &&
                robot.outtake.outtakePosition == OuttakePosition.TRANSFER

        var isRed = false
        var isBlue = false
        var isYellow = false

        val colorDetections = {
            isRed = sensor.isRed
            isBlue = sensor.isBlue
            isYellow = sensor.isYellow

            val colorDetections = isRed.toInt() + isBlue.toInt() + isYellow.toInt()
            if (colorDetections > 1) {
                Log.e("ColorDetection", "Red: $isRed, Blue: $isBlue, Yellow: $isYellow")
            }

            colorDetections
        }

        var colorDetectionTicks = 0

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
        follower.setStartingPose(autoPose)

        var holdHeading = false

        val delayedActions = ActionList<FunctionAction>()

        fun finishTransfer() {
            robot.claw.isClosed = true
            delayedActions += DelayedAction(250.milliseconds) { robot.intake.isClosed = false }
        }

        val pressActionList =
            ActionList(
                FunctionAction(controlGamepad::right_bumper) {
                    if (inTransfer()) {
                        if (robot.claw.isClosed) {
                            robot.intake.claw.isClosed = true
                            delayedActions +=
                                DelayedAction(250.milliseconds) {
                                    robot.claw.isClosed = false
                                }
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
                            delayedActions +=
                                DelayedAction(0.3.seconds) {
                                    robot.lift.targetPosition = Positions.Lift.up
                                    robot.outtake.outtakePosition = OuttakePosition.BASKET
                                }
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
                        delayedActions += DelayedAction(0.2.seconds) { setOuttakeLiftPosition() }
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
                FunctionAction(moveGamepad::right_bumper) { colorDetectionTicks = 0 },
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

            // Ordinea e intenționată, ultima din pressActionList pica daca e invers
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

                if (colorDetectionTicks <= 10 && robot.intake.isPickedUp) {
                    Log.d("ColorDetection", "Checking colors")
                    if (colorDetections() <= 1) {
                        RobotLog.d("ColorDetection", "Checked colors successfully")
                        if (!controlGamepad.cross && !controlGamepad.left_bumper) {
                            if (!sensor.isHoldingSample || isWrongColor) {
                                robot.intake.isClosed = false
                                RobotLog.d("ColorDetection", "Reached with no or wrong color specimen")
                            } else if (sensor.isHoldingSample && isRightColor) {
                                RobotLog.d("ColorDetection", "Reached correct color")
                                robot.intake.targetPosition = IntakePositions.TRANSFER
                            }
                        }
                        // Stops any further attempts at detecting colors
                        colorDetectionTicks = 10
                    }
                    colorDetectionTicks++
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
