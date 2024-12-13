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

    /**
     * @return Triple(Poziție pendul, Poziție lift, Poziție intake rotation)
     */
    // FIXME: Pozițiile la intake
    private fun basketPositions(pendulUp: Boolean) = when (pendulUp) {
        true -> Pair(PendulPosition.UP, LiftPosition.UP)
        false -> Pair(PendulPosition.DOWN, LiftPosition.DOWN)
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

        var pendulUp = false
        var ticksSincePendulChange = 0
        var ticksSinceIntakeChange = 0

        while(opModeIsActive()) {
            // Movement
            robot.move(moveGamepad)

            // Actions for other hardware (intake, lift, etc.)
            val telemetryPacket = TelemetryPacket()
            runActions(telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Pendul + Lift (Gamepad2.y)
            /* Delay de 10 ticks ca să nu acționeze prea repede butonul */
            when (ticksSincePendulChange) {
                in 0..10 -> ticksSincePendulChange += 1
                else -> if (controlGamepad.y) {
                    pendulUp = pendulUp xor controlGamepad.y
                    ticksSincePendulChange = 0
                }
            }

            val (pendulPosition, liftPosition) = basketPositions(pendulUp)
            robot.pendul.targetPosition = pendulPosition
            robot.lift.targetPosition = liftPosition


            // Extend
            robot.extend.power = moveGamepad.right_stick_y * MULTIPLIER_EXTEND


            // Intake Rotate
            val intakeRotatePosition = IntakeRotationPosition.PERPENDICULAR
            robot.intakeRotation.targetPosition = intakeRotatePosition

            // Intake
            val intakeActive = robot.intake.intakePower != 0.0
            when (ticksSinceIntakeChange) {
                in 0..10 -> ticksSinceIntakeChange += 1
                else -> if (controlGamepad.y || controlGamepad.b) {
                    robot.intake.intakePower = if (intakeActive) 0.0 else if (controlGamepad.y) 1.0 else -1.0
                }
            }

            robot.extend.power = controlGamepad.right_stick_y.toDouble()
        }
    }
}