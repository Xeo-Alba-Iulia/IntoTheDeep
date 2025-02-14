package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.util.Timer
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
    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)
        val resetTimer = Timer()
        var resetLift = false

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

        val clawControlToggle by TogglePress(controlGamepad::right_bumper)

        val resetLiftButton by SinglePress(controlGamepad::left_stick_button)

        waitForStart()

        dashboard.clearTelemetry()
        follower.startTeleopDrive()

        while (opModeIsActive()) {
            // Movement
            follower.setTeleOpMovementVectors(
                -moveGamepad.left_stick_y.toDouble(),
                -moveGamepad.left_stick_x.toDouble(),
                -moveGamepad.right_stick_x.toDouble(),
                false
            )
            follower.update()

            robot.applyPositions(controlGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
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
                gamepad1.right_bumper -> robot.intake.pickUp()
                gamepad1.left_bumper -> robot.intake.isClosed = false
            }

            robot.intake.clawRotate.targetPosition +=
                (gamepad1.right_trigger - gamepad1.left_trigger) * robot.intake.clawRotate.adjustMultiplier

            if (resetLiftButton) {
                robot.lift.targetPosition -= Positions.Lift.half
                resetTimer.resetTimer()
                resetLift = true
            }

            if (resetLift && resetTimer.elapsedTimeSeconds > 0.8) {
                robot.lift.resetLifts()
                resetLift = false
            }
            // if (!inTransfer()) {
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
                    gamepad.cross -> {
                        Pair(
                            outtake.outtakePosition,
                            IntakePositions.PICKUP
                        )
                    }

                    gamepad.dpad_right -> {
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
