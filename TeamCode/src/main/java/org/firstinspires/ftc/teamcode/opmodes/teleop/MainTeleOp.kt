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
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.PositionStore
import org.firstinspires.ftc.teamcode.util.PressAction

@TeleOp(name = "TeleOp", group = "A")
class MainTeleOp : LinearOpMode() {
    var openOuttakeClaw = false

    private fun MutableList<Pair<Long, Runnable>>.addDelayed(
        delay: Double,
        runnable: Runnable,
    ) = add(
        Pair(
            System.currentTimeMillis() + (delay * 1000).toLong(),
            runnable,
        ),
    )

    private fun MutableList<Pair<Long, Runnable>>.run() {
        val iterator = iterator()

        while (iterator.hasNext()) {
            val action = iterator.next()

            if (System.currentTimeMillis() >= action.first) {
                action.second.run()
                iterator.remove()
            }
        }
    }

    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)

        fun inTransfer() =
            robot.lift.targetPosition == Positions.Lift.transfer &&
                robot.outtake.outtakePosition == OuttakePosition.TRANSFER

        val headingPIDFCoefficients = FollowerConstants.headingPIDFCoefficients
        val headingPIDF = PIDFController(headingPIDFCoefficients)
        headingPIDF.targetPosition = 0.0

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

//        while (!isStarted) {
//            val telemetryPacket = TelemetryPacket()
//            telemetryPacket.addLine("Initializing")
//            runActions(actionList, telemetryPacket)
//            dashboard.sendTelemetryPacket(telemetryPacket)
//        }

        val follower = Follower(this.hardwareMap)
        follower.setStartingPose(PositionStore.pose)

//        val clawControlToggle by TogglePress {
//            if (controlGamepad.circle && !robot.claw.isClosed) {
//                true
//            } else {
//                controlGamepad.right_bumper
//            }
//        }
//
//        val intakePositionSet by SinglePress(controlGamepad::cross)
//        val holdHeadingButton by SinglePress(moveGamepad::cross)

        val delayedActions = mutableListOf<Pair<Long, Runnable>>()

        var holdHeading = false

        fun finishTransfer() {
            robot.claw.isClosed = true
            delayedActions.addDelayed(0.25) { robot.intake.isClosed = false }
        }

        val pressActionList =
            listOf(
                PressAction(controlGamepad::right_bumper) {
                    if (inTransfer()) {
                        if (robot.claw.isClosed) {
                            robot.intake.claw.isClosed = true
                            delayedActions.addDelayed(0.25) { robot.claw.isClosed = false }
                        } else {
                            finishTransfer()
                        }
                    } else {
                        robot.claw.isClosed = !robot.claw.isClosed
                    }

                    if (robot.claw.isClosed) {
                        holdHeading = false
                    }
                },
                PressAction(controlGamepad::cross, robot.intake::switch),
                PressAction(moveGamepad::cross) {
//                    if (!robot.claw.isClosed) {
                    holdHeading = !holdHeading
//                    }
                },
                PressAction(controlGamepad::right_stick_button) {
                    robot.lift.targetPosition = Positions.Lift.up
                    robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                },
                PressAction(controlGamepad::left_stick_button) {
                    robot.lift.targetPosition = Positions.Lift.hang
                },
                PressAction(controlGamepad::circle) {
                    if (inTransfer()) {
                        if (!robot.claw.isClosed) {
                            finishTransfer()
                            delayedActions.addDelayed(0.3) {
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
                PressAction({ -controlGamepad.left_stick_y < -0.7 }) {
                    // Joystick-urile sunt inversate
                    robot.lift.targetPosition = Positions.Lift.half
                    robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                    robot.claw.isClosed = false
                },
                PressAction(controlGamepad::dpad_right) {
                    robot.intake.targetPosition = IntakePositions.TRANSFER
                    delayedActions.addDelayed(1.1) {
                        robot.outtake.outtakePosition = OuttakePosition.TRANSFER
                        robot.lift.targetPosition = Positions.Lift.transfer
                    }
                },
            )

        waitForStart()

        dashboard.clearTelemetry()
        follower.startTeleopDrive()

        while (opModeIsActive()) {
            val powerMultiply = if (robot.intake.targetPosition == IntakePositions.PICKUP) 0.37 else 1.0

            // Movement
            follower.setTeleOpMovementVectors(
                -moveGamepad.left_stick_y.toDouble(),
                -moveGamepad.left_stick_x.toDouble() * powerMultiply,
                if (holdHeading) {
                    follower.headingOffset += moveGamepad.right_stick_x.toDouble() * 0.025

                    val modifiedHeading =
                        if (follower.pose.heading <= Math.PI) {
                            follower.pose.heading
                        } else {
                            -2 * Math.PI + follower.pose.heading
                        }

                    RobotLog.d("Modified PID heading: $modifiedHeading")

                    headingPIDF.updatePosition(modifiedHeading)
                    headingPIDF.runPIDF()
                } else {
                    -moveGamepad.right_stick_x.toDouble() * powerMultiply
                },
                false,
            )
            follower.update()
            follower.drawOnDashBoard()

            for (action in pressActionList) {
                action.run()
            }

            delayedActions.run()

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

            when {
                gamepad1.right_bumper -> {
                    robot.intake.pickUp()
                }

                gamepad1.left_bumper -> {
                    robot.intake.isClosed = false
                    robot.intake.clawRotate.targetPosition = Positions.IntakeClawRotate.middle
                }
            }

//            robot.intake.clawRotate.targetPosition +=
//                (gamepad1.right_trigger - gamepad1.left_trigger) * robot.intake.clawRotate.adjustMultiplier

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
                    gamepad.dpad_right -> Positions.Lift.transfer
                    else -> lift.targetPosition
                }

//            if (gamepad.circle) {
//                outtake.outtakePosition = OuttakePosition.TRANSFER
//            }

//            when {
//                gamepad.dpad_right -> intakePendul.targetPosition = Positions.IntakePendul.transfer
//                gamepad.cross -> intakePendul.targetPosition = Positions.IntakePendul.pickup
//                gamepad.triangle -> intakePendul.targetPosition = Positions.IntakePendul.init
//            }

            // Stick positions are inverted
//            if (gamepad.right_stick_y + gamepad.left_stick_y <= -0.4) {
//                extend.targetPosition = Positions.Extend.out
//            }

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
//            intakePendul.targetPosition = intakePendulPosition as Double
        } catch (e: NotImplementedError) {
            // Driver station nu arata NotImplementedError, doar oprește OpMode
            throw RuntimeException(e)
        }
    }
}
