package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePosition
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.Extend
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import java.util.concurrent.TimeUnit

@TeleOp(name = "TeleOp", group = "A")
class MainTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)

        fun inTransfer() =
            robot.intake.intakePosition == IntakePosition.TRANSFER &&
                robot.outtake.outtakePosition == OuttakePosition.TRANSFER

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        val actionList =
            mutableListOf(
                robot.intake,
                robot.outtake,
                robot.extend,
                robot.lift
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
            robot.lift.power =
                (
                    (moveGamepad.right_trigger - moveGamepad.left_trigger) *
                        (moveGamepad.right_trigger + moveGamepad.left_trigger)
                ).toDouble()

            // Intake Power
            robot.intake.intakePower =
                when {
                    moveGamepad.a -> 0.8
                    moveGamepad.x -> -1.0
                    else -> 0.07
                }

            // Pendul manual
//            robot.pendul.targetPosition -= controlGamepad.left_stick_y * Pendul.MULTIPLIER

            // Extend
            robot.extend.targetPosition +=
                (controlGamepad.right_trigger - controlGamepad.left_trigger) * Extend.MULTIPLIER

            if (controlGamepad.x && inTransfer()) {
                // FIXME: S-ar putea să fie nevoie de ceva timing aici
                robot.intake.intakePower = -0.05
                robot.claw.targetPosition = Positions.Claw.close
            }

            // if (!inTransfer()) {
            robot.claw.targetPosition =
                when {
                    moveGamepad.right_bumper || controlGamepad.right_bumper -> Positions.Claw.close
                    moveGamepad.left_bumper || controlGamepad.left_bumper -> Positions.Claw.open
                    else -> robot.claw.targetPosition
                }
            // }
        }
    }

    /**
     * Runs all Actions in [actionList], deleting every finished [Action]
     */
    fun runActions(
        actionList: MutableList<Action>,
        telemetryPacket: TelemetryPacket,
    ) {
        val iterator = actionList.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (!action.run(telemetryPacket)) iterator.remove()
        }
    }

    private val timer = ElapsedTime()
    private var pendulIsActioned = false
    private var intakeIsUp = false

    /**
     * Apply positions to the robot hardware based on the gamepad input
     *
     * @throws RuntimeException if the target position is not implemented
     * (workaround for DriverStation not displaying NotImplementedException)
     */
    fun RobotHardware.applyPositions(gamepad: Gamepad) {
        if (gamepad.dpad_down && intakeIsUp) {
            intakeIsUp = false
            timer.reset()
        }

        intake.intakePosition =
            when {
                gamepad.dpad_down -> IntakePosition.INTAKE
                gamepad.dpad_right -> IntakePosition.TRANSFER
                else -> intake.intakePosition
            }

        if (intake.intakePosition != IntakePosition.TRANSFER) {
            intake.intakePosition =
                when {
                    extend.targetPosition > 0.3 &&
                        timer.time(
                            TimeUnit.MILLISECONDS
                        ) >= 500 -> IntakePosition.INTAKE

                    extend.targetPosition <= 0.3 &&
                        timer.time(
                            TimeUnit.MILLISECONDS
                        ) >= 500 -> IntakePosition.ENTRANCE

                    else -> IntakePosition.ENTRANCE
                }
        }

        if (gamepad.dpad_right && !pendulIsActioned) {
            pendulIsActioned = true
            intakeIsUp = true
            timer.reset()
        }

        if (pendulIsActioned && timer.time(TimeUnit.MILLISECONDS) >= 500) {
            outtake.pendul.targetPosition = Positions.Pendul.transfer
            pendulIsActioned = false
        }

        try {
            val (outtakePosition, extendPosition, clawPosition) =
                when {
                    gamepad.dpad_right -> {
                        listOf(
                            OuttakePosition.TRANSFER,
                            Positions.Extend.`in`,
                            claw.targetPosition
                        )
                    }

                    gamepad.dpad_up -> {
                        listOf(
                            OuttakePosition.OUTTAKE,
                            extend.targetPosition,
                            claw.targetPosition
                        )
                    }

                    gamepad.dpad_left -> {
                        listOf(
                            OuttakePosition.SMASH,
                            extend.targetPosition,
                            Positions.Claw.open
                        )
                    }

                    gamepad.cross -> {
                        listOf(
                            OuttakePosition.BASKET,
                            extend.targetPosition,
                            claw.targetPosition
                        )
                    }

                    else -> {
                        listOf(
                            outtake.outtakePosition,
                            extend.targetPosition,
                            claw.targetPosition
                        )
                    }
                }
            outtake.outtakePosition = outtakePosition as OuttakePosition
            extend.targetPosition = extendPosition as Double
            claw.targetPosition = clawPosition as Double
        } catch (e: NotImplementedError) {
            // Driver station nu arata NotImplementedError, doar oprește OpMode
            throw RuntimeException(e)
        }
    }
}
