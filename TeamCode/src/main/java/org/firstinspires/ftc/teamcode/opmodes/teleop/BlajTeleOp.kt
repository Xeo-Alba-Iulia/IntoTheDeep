package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.hardware.intake.IntakeRotationPosition

private const val MULTIPLIER_EXTEND = 0.003

@TeleOp
class BlajTeleOp : LinearOpMode() {
    val actionList = mutableListOf<Action>()

    private fun runActions(telemetryPacket: TelemetryPacket) {
        val iterator = actionList.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (!action.run(telemetryPacket)) iterator.remove()
        }
    }

    override fun runOpMode() {
        val robot = RobotHardware(hardwareMap)

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        waitForStart()

        actionList.addAll(
            listOf(
                robot.intake,
                robot.intakeRotation,
                robot.pendul,
                robot.extend
            )
        )

        while(opModeIsActive()) {
            // Movement
            robot.move(moveGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
            runActions(telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Pendul + Lift (Gamepad2.up_button)
//            val (pendulPosition, liftPosition) = when {
//                controlGamepad.dpad_up -> Pair(PendulPosition., LiftPosition.UP)
//            }

            // Extend
            robot.extend.power = moveGamepad.right_stick_y * MULTIPLIER_EXTEND

            // Intake Rotate
            val intakeRotatePosition = IntakeRotationPosition.PERPENDICULAR
            robot.intakeRotation.targetPosition = intakeRotatePosition

            // Intake
            robot.intake.intakePower = when {
                moveGamepad.a -> 1.0
                moveGamepad.x -> -1.0
                else -> 0.0
            }

            robot.extend.power = (controlGamepad.right_trigger - controlGamepad.left_trigger).toDouble()
        }
    }
}