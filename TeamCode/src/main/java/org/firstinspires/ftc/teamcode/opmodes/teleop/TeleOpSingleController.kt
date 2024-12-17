package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.subsystems.lift.LiftPosition
import org.firstinspires.ftc.teamcode.subsystems.pendul.PendulPosition

private const val MULTIPLIER_PENDUL = 0.001

@TeleOp
class TeleOpSingleController : LinearOpMode() {
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
                robot.pendul.pendul,
                robot.extend,
//                robot.lift.liftManual
            )
        )

        robot.lift.lift.targetPosition = PendulPosition.DOWN.positionValue

        while (opModeIsActive()) {
            // Movement
            robot.move(moveGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
            runActions(telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Pendul + Lift (Gamepad2.up_button)
            val (pendulPosition, liftPosition, intakeRotatePosition) = when {
                moveGamepad.dpad_up -> Triple(
                    PendulPosition.BAR.positionValue,
                    LiftPosition.UP,
                    IntakeRotationPosition.PARALLEL
                )

                moveGamepad.dpad_left -> Triple(
                    PendulPosition.BAR.positionValue,
                    LiftPosition.HALF,
                    IntakeRotationPosition.PERPENDICULAR
                )

                moveGamepad.dpad_down -> Triple(
                    PendulPosition.DOWN.positionValue,
                    LiftPosition.DOWN,
                    IntakeRotationPosition.PARALLEL
                )

                else -> Triple(
                    robot.pendul.pendul.targetPosition,
                    robot.lift.targetPosition,
                    robot.intakeRotation.targetPosition
                )
            }

            robot.pendul.pendul.targetPosition = pendulPosition
            robot.lift.targetPosition = liftPosition
            robot.intakeRotation.targetPosition = intakeRotatePosition

            // Lift
            robot.lift.lift.power = (moveGamepad.right_trigger - moveGamepad.left_trigger).toDouble()

            // Intake
            robot.intake.intakePower = when {
                moveGamepad.a -> 0.6
                moveGamepad.x -> -1.0
                else -> 0.0
            }

            // Pendul manual
            robot.pendul.pendul.targetPosition -= (if (moveGamepad.right_bumper) 1.0 else 0.0 - if (moveGamepad.left_bumper) 1.0 else 0.0) * MULTIPLIER_PENDUL

            robot.extend.power = (moveGamepad.left_trigger - moveGamepad.right_trigger).toDouble()
        }
    }
}