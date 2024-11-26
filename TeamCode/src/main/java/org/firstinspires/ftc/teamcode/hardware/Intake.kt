package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    companion object {
        private const val START_POWER = 0.2
    }

    private val intakeMotor = hardwareMap.crservo.get("IntakeMotor")
    private val intakeRotation = hardwareMap.servo.get("IntakeRotation")

    interface IntakeAction : Action {
        var intakePower: Double
        fun setPowerAction(power: Double): Action
        fun cancel()
    }

    val intake = object : IntakeAction {
        private var isCanceled = false
        override var intakePower: Double = START_POWER
            set(value) {
                require(value in 0.0..1.0) { "Intake power must be between 0 and 1" }
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

        override fun cancel() {
            isCanceled = true
        }

        override fun setPowerAction(power: Double): Action = actionWrapper { intakePower = power }
    }

    interface IntakeRotateAction : Action {
        var position: Double
        fun setPositionAction(position: Double): Action
        fun cancel()
    }

    val intakeRotate = object : IntakeRotateAction {
        private var isCanceled = false
        override var position = 0.0
            set(value) {
                require(value in 0.0..1.0) { "Intake position must be between 0 and 1" }
                field = value
            }

        override fun run(p: TelemetryPacket): Boolean {
            if (isCanceled) {
                isCanceled = false
                return false
            }

            intakeRotation.position = position
            return true
        }

        override fun cancel() {
            isCanceled = true
        }

        override fun setPositionAction(position: Double): Action = actionWrapper { this.position = position }
    }

    private fun actionWrapper(block: () -> Unit): Action = object : Action {
        override fun run(p: TelemetryPacket): Boolean {
            block()
            return false
        }
    }

    private fun actionWrapperTelemetry(block: (TelemetryPacket) -> Unit): Action = object : Action {
        override fun run(p: TelemetryPacket): Boolean {
            block(p)
            return false
        }
    }
}