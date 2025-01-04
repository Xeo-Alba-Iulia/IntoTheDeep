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
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

@TeleOp
class MainTeleOp : LinearOpMode() {
    private val actionList = mutableListOf<Action>()

    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        actionList.addAll(
            listOf(
                robot.intake,
                robot.pendul,
                robot.extend,
                robot.lift,
                robot.pendul,
                robot.claw,
                robot.clawRotate
            )
        )

        while (!isStarted) {
            val telemetryPacket = TelemetryPacket()
            telemetryPacket.addLine("Initializing")
            runActions(telemetryPacket)
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
            runActions(telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Lift
            robot.lift.power = (moveGamepad.right_trigger - moveGamepad.left_trigger).toDouble()

            // Intake Power
            robot.intake.intakePower = when {
                moveGamepad.a -> 0.8
                moveGamepad.x -> -1.0
                else -> 0.07
            }

            // Pendul manual
//            robot.pendul.targetPosition -= controlGamepad.left_stick_y * Pendul.MULTIPLIER

            // Extend
            robot.extend.targetPosition -= controlGamepad.right_stick_y * Extend.MULTIPLIER

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

    private fun runActions(telemetryPacket: TelemetryPacket) {
        val iterator = actionList.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (!action.run(telemetryPacket)) iterator.remove()
        }
    }

    private fun RobotHardware.applyPositions(gamepad: Gamepad) {
        intake.intakePosition = when {
            gamepad.dpad_down -> IntakePosition.INTAKE
            gamepad.dpad_right -> IntakePosition.TRANSFER
            else -> intake.intakePosition
        }

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
    }
}