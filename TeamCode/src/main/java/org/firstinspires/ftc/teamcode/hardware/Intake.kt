package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) : Action {
    companion object {
        private const val START_POWER = 0.2
    }

    private val intakeMotor = hardwareMap.crservo.get("IntakeMotor")
    private val intakeRotation = hardwareMap.servo.get("IntakeRotation")

    init {
        intakeMotor.direction = DcMotorSimple.Direction.REVERSE
    }

    // Implementing Action interface for the actual Intake

    private var isCanceled = false

    /**
     * Power to set the intake to
     */
    var intakePower: Double = START_POWER
        set(value) {
            require(value in -1.0..1.0) { "Intake power must be between 0 and 1" }
            field = value
        }

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false

            intakeMotor.power = 0.0
            intakePower = START_POWER
            return false
        }

        intakeMotor.power = intakePower
        return true
    }

    val rotate = object : ManualPositionMechanism {
        private var isCanceled = false
        override var targetPosition = 0.0
            set(value) {
                require(value in 0.0..1.0) { "Intake position must be between 0 and 1" }
                field = value
            }

        override fun run(p: TelemetryPacket): Boolean {
            if (isCanceled) {
                isCanceled = false
                return false
            }

            intakeRotation.position = targetPosition
            return true
        }

        override fun cancel() {
            isCanceled = true
        }
    }
}