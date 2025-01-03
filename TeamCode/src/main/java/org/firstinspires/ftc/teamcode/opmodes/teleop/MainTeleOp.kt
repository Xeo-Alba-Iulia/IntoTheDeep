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

    private fun runActions(telemetryPacket: TelemetryPacket) {
        val iterator = actionList.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (!action.run(telemetryPacket)) iterator.remove()
        }
    }

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
            runActions(telemetryPacket)
            telemetryPacket.addLine("Initializing")
            dashboard.sendTelemetryPacket(telemetryPacket)
        }

        waitForStart()

        dashboard.telemetry.clearAll()

        while (opModeIsActive()) {
            // Movement
            robot.movement.move(moveGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
            runActions(telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Intake position
            robot.intake.intakePosition = when {
                controlGamepad.dpad_down -> IntakePosition.INTAKE
                controlGamepad.dpad_right -> IntakePosition.TRANSFER
                else -> robot.intake.intakePosition
            }

            // Lift
            robot.lift.power = (moveGamepad.right_trigger - moveGamepad.left_trigger).toDouble()

            // Intake Power
            robot.intake.intakePower = when {
                moveGamepad.a -> 0.6
                moveGamepad.x -> -1.0
                else -> 0.1
            }

            // Pendul manual
//            robot.pendul.targetPosition -= controlGamepad.left_stick_y * Pendul.MULTIPLIER

            // Extend
            robot.extend.targetPosition = when {
                controlGamepad.dpad_down -> Positions.Extend.`in`
                controlGamepad.dpad_right -> Positions.Extend.out
                else -> robot.extend.targetPosition
            }
            robot.extend.targetPosition -= controlGamepad.right_stick_y * Extend.MULTIPLIER

            // Pendul
            robot.pendul.targetPosition = when {
                controlGamepad.dpad_down -> Positions.Pendul.outtake
                controlGamepad.dpad_right -> Positions.Pendul.transfer
                else -> robot.pendul.targetPosition
            }
        }
    }
}