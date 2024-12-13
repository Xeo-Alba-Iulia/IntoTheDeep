package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.hardware.intake.IntakeRotationPosition
import org.firstinspires.ftc.teamcode.hardware.lift.LiftPosition
import org.firstinspires.ftc.teamcode.hardware.pendul.PendulPosition

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
            val (pendulPosition, liftPosition, intakeRotatePosition) = when {
                controlGamepad.dpad_up -> Triple(
                    PendulPosition.BASKET,
                    LiftPosition.UP,
                    IntakeRotationPosition.PARALLEL
                )

                controlGamepad.dpad_left -> Triple(
                    PendulPosition.BAR,
                    LiftPosition.HALF,
                    IntakeRotationPosition.PERPENDICULAR
                )

                controlGamepad.dpad_down -> Triple(
                    PendulPosition.DOWN,
                    LiftPosition.DOWN,
                    IntakeRotationPosition.PARALLEL
                )

                else -> Triple(
                    robot.pendul.targetPosition,
                    robot.lift.targetPosition,
                    robot.intakeRotation.targetPosition
                )
            }
            robot.pendul.targetPosition = pendulPosition
            robot.lift.targetPosition = liftPosition
            robot.intakeRotation.targetPosition = intakeRotatePosition

            // Extend
            robot.extend.power = moveGamepad.right_stick_y * MULTIPLIER_EXTEND

            // Intake
            robot.intake.intakePower = when {
                moveGamepad.a -> 1.0
                moveGamepad.x -> -1.0
                else -> 0.0
            }

            robot.extend.power = (controlGamepad.left_trigger - controlGamepad.right_trigger).toDouble()
        }
    }
}