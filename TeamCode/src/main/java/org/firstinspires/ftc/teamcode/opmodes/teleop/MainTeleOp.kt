package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.util.PositionStore
import org.firstinspires.ftc.teamcode.util.SinglePress

@TeleOp(name = "TeleOp", group = "A")
class MainTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val robot = RobotHardware(this.hardwareMap)

        fun inTransfer() =
//            robot.intake.intakePosition == IntakePosition.TRANSFER &&
            robot.outtake.outtakePosition == OuttakePosition.TRANSFER

        val moveGamepad: Gamepad = gamepad1
        val controlGamepad: Gamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        val actionList =
            mutableListOf(
                robot.intake,
                robot.intakePendul,
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

        val clawControlToggle by SinglePress(controlGamepad::right_bumper)
        val clawMovementOpen by SinglePress(moveGamepad::left_bumper)
        val clawMovementClose by SinglePress(moveGamepad::right_bumper)

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

            // Lift

            // Intake Power
            robot.intake.intakePower =
                when {
                    moveGamepad.a -> 0.8
                    moveGamepad.x -> -1.0
                    else -> 0.0
                }

            // Pendul manual
//            robot.pendul.targetPosition -= controlGamepad.left_stick_y * Pendul.MULTIPLIER

            // Extend
            robot.extend.targetPosition +=
                (controlGamepad.right_trigger - controlGamepad.left_trigger) * robot.extend.adjustMultiplier

//            if (controlGamepad.x && inTransfer()) {
//                // FIXME: S-ar putea să fie nevoie de ceva timing aici
//                robot.intake.intakePower = -0.05
//                robot.claw.targetPosition = Positions.Claw.close
//            }

            robot.claw.isClosed =
                ((robot.claw.isClosed xor clawControlToggle) || clawMovementClose) &&
                !clawMovementOpen

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

    private val timer = ElapsedTime()
    private var pendulIsActioned = false
    private var intakeIsUp = false

    /**
     * Apply positions to the robot hardware based on the gamepad input
     *
     * @throws RuntimeException if the target position is not implemented
     * (workaround for DriverStation not displaying NotImplementedException)
     */
    private fun RobotHardware.applyPositions(gamepad: Gamepad) {
//        if (gamepad.dpad_down && intakeIsUp) {
//            intakeIsUp = false
//            timer.reset()
//        }

        lift.targetPosition =
            when {
                gamepad.square -> Positions.Lift.down
                gamepad.triangle || gamepad.dpad_right -> Positions.Lift.half
                gamepad.circle -> Positions.Lift.up
                else -> lift.targetPosition
            }

        when {
            gamepad.dpad_right -> intakePendul.targetPosition = Positions.IntakePendul.transfer
            gamepad.cross -> intakePendul.targetPosition = Positions.IntakePendul.pickup
            gamepad.triangle -> intakePendul.targetPosition = Positions.IntakePendul.init
        }

        // Stick positions are inverted
        if (gamepad.right_stick_y + gamepad.left_stick_y <= -0.4) {
            extend.targetPosition = Positions.Extend.out
        }

//        intake.intakePosition =
//            when {
//                gamepad.dpad_down -> IntakePosition.INTAKE
//                gamepad.dpad_right -> IntakePosition.TRANSFER
//                else -> intake.intakePosition
//            }

//        if (intake.intakePosition != IntakePosition.TRANSFER) {
//            intake.intakePosition =
//                when {
//                    extend.targetPosition > 0.3 &&
//                        timer.time(
//                            TimeUnit.MILLISECONDS
//                        ) >= 500 -> IntakePosition.INTAKE
//
//                    extend.targetPosition <= 0.3 &&
//                        timer.time(
//                            TimeUnit.MILLISECONDS
//                        ) >= 500 -> IntakePosition.ENTRANCE
//
//                    else -> IntakePosition.ENTRANCE
//                }
//        }

//        if (gamepad.dpad_right && !pendulIsActioned) {
//            pendulIsActioned = true
//            intakeIsUp = true
//            timer.reset()
//        }
//
//        if (pendulIsActioned && timer.time(TimeUnit.MILLISECONDS) >= 500) {
//            outtake.pendul.targetPosition = Positions.Pendul.transfer
//            pendulIsActioned = false
//        }

        try {
            val (outtakePosition, extendPosition, intakePendulPosition) =
                when {
                    gamepad.dpad_right -> {
                        listOf(
                            OuttakePosition.TRANSFER,
                            Positions.Extend.`in`,
                            Positions.IntakePendul.transfer
                        )
                    }

                    gamepad.dpad_up -> {
                        listOf(
                            OuttakePosition.BASKET,
                            Positions.Extend.`in`,
                            Positions.IntakePendul.init
                        )
                    }

                    gamepad.dpad_left -> {
                        listOf(
                            OuttakePosition.BAR,
                            Positions.Extend.`in`,
                            Positions.IntakePendul.init
                        )
                    }

                    gamepad.dpad_down -> {
                        listOf(
                            OuttakePosition.PICKUP,
                            Positions.Extend.`in`,
                            Positions.IntakePendul.init
                        )
                    }

                    else -> {
                        listOf(
                            outtake.outtakePosition,
                            extend.targetPosition,
                            intakePendul.targetPosition
                        )
                    }
                }
            outtake.outtakePosition = outtakePosition as OuttakePosition
            extend.targetPosition = extendPosition as Double
            intakePendul.targetPosition = intakePendulPosition as Double
        } catch (e: NotImplementedError) {
            // Driver station nu arata NotImplementedError, doar oprește OpMode
            throw RuntimeException(e)
        }
    }
}
