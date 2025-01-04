package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.subsystems.IntakeMotor
import org.firstinspires.ftc.teamcode.subsystems.IntakePendul
import org.firstinspires.ftc.teamcode.subsystems.IntakeRotation
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

class Intake(hardwareMap: HardwareMap) : Action {
    private val intakeMotor = IntakeMotor(hardwareMap)
    private val intakeRotation = IntakeRotation(hardwareMap)
    private val intakePendul = IntakePendul(hardwareMap)

    override fun run(p: TelemetryPacket) = intakePendul.run(p) && intakeRotation.run(p) && intakeMotor.run(p)

    var intakePower: Double
        get() = intakeMotor.intakePower
        set(value) {
            intakeMotor.intakePower = value
        }

    var intakePosition: IntakePosition
        get() = when (intakePendul.targetPosition) {
            Positions.IntakePendul.up -> IntakePosition.TRANSFER
            else -> IntakePosition.INTAKE
        }
        set(value) {
            when (value) {
                IntakePosition.INTAKE -> {
                    intakePendul.targetPosition = Positions.IntakePendul.down
                    intakeRotation.targetPosition = Positions.IntakeRotation.parallel
                }

                IntakePosition.TRANSFER -> {
                    intakePendul.targetPosition = Positions.IntakePendul.up
                    intakeRotation.targetPosition = Positions.IntakeRotation.perpendicular
                }
            }
        }
}

enum class IntakePosition {
    INTAKE,
    TRANSFER
}

@TeleOp
class IntakeTest : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()
        val intake = Intake(hardwareMap)

        while (opModeIsActive()) {
            intake.intakePower = gamepad1.left_stick_y.toDouble()
            intake.intakePosition = when {
                gamepad1.a -> IntakePosition.INTAKE
                gamepad1.b -> IntakePosition.TRANSFER
                else -> intake.intakePosition
            }

            val packet = TelemetryPacket()
            intake.run(packet)
            telemetry.addData("Intake Position", intake.intakePosition)
            telemetry.update()
        }
    }
}