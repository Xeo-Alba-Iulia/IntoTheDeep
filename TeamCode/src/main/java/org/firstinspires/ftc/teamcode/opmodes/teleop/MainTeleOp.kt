package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.IntakePosition
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.subsystems.Extend
import org.firstinspires.ftc.teamcode.subsystems.Lift
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

@TeleOp
class MainTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        val actionList = mutableListOf(
            robot.intake,
            robot.pendul,
            robot.extend,
            robot.lift,
            robot.pendul,
            robot.claw,
            robot.clawRotate
        )

        while (!isStarted) {
            val telemetryPacket = TelemetryPacket()
            telemetryPacket.addLine("Initializing")
            runActions(actionList, telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)
        }

        waitForStart()

        dashboard.clearTelemetry()

        while (opModeIsActive()) {
            // Movement
            robot.movement.move(moveGamepad)
            robot.applyPositions(controlGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
            runActions(actionList, telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Lift
            robot.lift.power = (moveGamepad.right_trigger - moveGamepad.left_trigger).toDouble()
            robot.lift.power = if (moveGamepad.left_trigger < 0.1) {
                moveGamepad.right_trigger.toDouble()
            } else {
                Lift.staticPower(moveGamepad.left_trigger.toDouble())
            }

            // Intake Power
            robot.intake.intakePower = when {
                moveGamepad.a -> 0.8
                moveGamepad.x -> -1.0
                else -> 0.07
            }

            // Pendul manual
//            robot.pendul.targetPosition -= controlGamepad.left_stick_y * Pendul.MULTIPLIER

            // Extend
            // FIXME: E necesar Extend.MULTIPLIER sau nu
            robot.extend.targetPosition -= (controlGamepad.right_trigger - controlGamepad.left_trigger)

            if (controlGamepad.x &&
                robot.pendul.targetPosition == Positions.Pendul.transfer &&
                robot.intake.intakePosition == IntakePosition.TRANSFER
            ) {
                // FIXME: S-ar putea să fie nevoie de ceva timing aici
                robot.intake.intakePower = -0.05
                robot.claw.targetPosition = Positions.Claw.close
            }
        }
    }

    /**
     * Runs all Actions in [actionList], deleting every finished [Action]
     */
    fun runActions(actionList: MutableList<Action>, telemetryPacket: TelemetryPacket) {
        val iterator = actionList.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (!action.run(telemetryPacket)) iterator.remove()
        }
    }

    /**
     * Apply positions to the robot hardware based on the gamepad input
     *
     * @throws RuntimeException if the target position is not implemented
     * (workaround for DriverStation not displaying NotImplementedException)
     */
    fun RobotHardware.applyPositions(gamepad: Gamepad) {
        intake.intakePosition = when {
            gamepad.dpad_down -> IntakePosition.INTAKE
            gamepad.dpad_right -> IntakePosition.TRANSFER
            else -> intake.intakePosition
        }

        try {
            val (pendulPosition, extendPosition, clawPosition, clawRotatePosition) = when {
                gamepad.dpad_down -> listOf(
                    pendul.targetPosition,
                    Positions.Extend.out,
                    claw.targetPosition,
                    clawRotate.targetPosition
                )

                gamepad.dpad_right -> listOf(
                    Positions.Pendul.transfer,
                    Positions.Extend.`in`,
                    Positions.Claw.open,
                    Positions.ClawRotate.transfer
                )

                gamepad.dpad_up -> listOf(
                    Positions.Pendul.outtake,
                    extend.targetPosition,
                    claw.targetPosition,
                    Positions.ClawRotate.outtake
                )

                else -> listOf(
                    pendul.targetPosition,
                    extend.targetPosition,
                    claw.targetPosition,
                    clawRotate.targetPosition
                )
            }
            pendul.targetPosition = pendulPosition
            extend.targetPosition = extendPosition
            claw.targetPosition = clawPosition
            clawRotate.targetPosition = clawRotatePosition
        } catch (e: NotImplementedError) {
            // Driver station nu arata NotImplementedError, doar oprește OpMode
            throw RuntimeException(e)
        }
    }
}