package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range.clip

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor = hardwareMap.crservo.get("IntakeMotor")
//    private val servo = hardwareMap.servo.get("IntakeServo")

    private var intakePower = 0.2
    private val startPower = 0.2

    private var isCanceled = false
    private var isStarted = false

    inner class IntakeAction : Action {
        override fun run(p: TelemetryPacket): Boolean {
            isStarted = true
            if (isCanceled) {
                isStarted = false
                isCanceled = false

                intakeMotor.power = 0.0
                return false
            }

            intakeMotor.power = intakePower
            return true
        }
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

    /**
     * Starts the intake and resets the power
     *
     * @return [IntakeAction] object
     * @throws IllegalStateException if the intake is already started
     */
    fun start() = if (!isStarted) {
        intakePower = startPower
        IntakeAction()
    } else throw IllegalStateException("Intake already started")

    /**
     * Stops the intake
     */
    fun stop() = actionWrapper {
        isCanceled = true
    }

    /**
     * Sets power of intakeMotor, use only temporarily
     */
    fun setPower(power: Double) = actionWrapperTelemetry { packet ->
        intakePower = clip(power, -1.0, 1.0)
        packet.put("Intake Power changed", intakePower)
    }
}