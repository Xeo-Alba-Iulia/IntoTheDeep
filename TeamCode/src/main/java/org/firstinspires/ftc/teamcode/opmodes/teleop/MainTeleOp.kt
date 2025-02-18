package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.PositionStore
import org.firstinspires.ftc.teamcode.util.SinglePress
import org.firstinspires.ftc.teamcode.util.TogglePress

@TeleOp(name = "TeleOp", group = "A")
class MainTeleOp : LinearOpMode() {
    var openOuttakeClaw = false

    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        val actionList =
            mutableListOf(
                robot.intake,
                robot.outtake,
                robot.extend,
                robot.lift,
                robot.claw
            )

        while (!isStarted) {
            val telemetryPacket = TelemetryPacket()
            telemetryPacket.addLine("Initializing")
            runActions(actionList, telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)
        }

        val follower = Follower(this.hardwareMap)
        follower.setStartingPose(PositionStore.pose)

        val clawControlToggle by TogglePress {
            controlGamepad.right_bumper
        }

        val intakePositionSet by SinglePress(controlGamepad::cross)

        waitForStart()

        dashboard.clearTelemetry()
        follower.startTeleopDrive()

        while (opModeIsActive()) {
            val powerMultiply = if (robot.intake.targetPosition == IntakePositions.PICKUP) 0.37 else 1.0

            // Movement
            follower.setTeleOpMovementVectors(
                -moveGamepad.left_stick_y.toDouble(),
                -moveGamepad.left_stick_x.toDouble() * powerMultiply,
                -moveGamepad.right_stick_x.toDouble() * powerMultiply,
                false
            )
            follower.update()
            follower.drawOnDashBoard()

            robot.applyPositions(controlGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
            telemetryPacket.put("Robot Pose", follower.pose.asPedroCoordinates)
            runActions(actionList, telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            if (moveGamepad.left_stick_button) {
                follower.pose = Pose(0.0, 0.0, 0.0)
            }

            val clawControlCache = clawControlToggle

            robot.outtake.claw.isClosed = clawControlCache

            if (robot.intake.targetPosition == IntakePositions.TRANSFER &&
                robot.outtake.outtakePosition == OuttakePosition.TRANSFER &&
                robot.lift.targetPosition == Positions.Lift.down
            ) {
                robot.intake.isClosed = !clawControlCache
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

            if (intakePositionSet) {
                val intake = robot.intake
                intake.targetPosition =
                    if (intake.targetPosition == IntakePositions.PICKUP) {
                        IntakePositions.TRANSFER
                    } else {
                        IntakePositions.PICKUP
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
                    gamepad.triangle || gamepad.dpad_right -> Positions.Lift.half
                    gamepad.circle -> Positions.Lift.up
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

            val (outtakePosition, intakePosition) =
                when {
                    gamepad.dpad_right -> {
                        openOuttakeClaw = true

                        Pair(
                            OuttakePosition.TRANSFER,
                            IntakePositions.TRANSFER
                        )
                    }

                    gamepad.dpad_up -> {
                        Pair(
                            OuttakePosition.BASKET,
                            intake.targetPosition
                        )
                    }

                    gamepad.dpad_left -> {
                        Pair(
                            OuttakePosition.BAR,
                            intake.targetPosition
                        )
                    }

                    gamepad.dpad_down -> {
                        Pair(
                            OuttakePosition.PICKUP,
                            intake.targetPosition
                        )
                    }

                    else -> {
                        Pair(
                            outtake.outtakePosition,
                            intake.targetPosition
                        )
                    }
                }
            outtake.outtakePosition = outtakePosition
            intake.targetPosition = intakePosition
//            intakePendul.targetPosition = intakePendulPosition as Double
        } catch (e: NotImplementedError) {
            // Driver station nu arata NotImplementedError, doar oprește OpMode
            throw RuntimeException(e)
        }
    }
}
