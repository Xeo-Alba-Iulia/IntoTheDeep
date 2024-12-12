package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.actionUtil.actionWrapper

class Intake(hardwareMap: HardwareMap) : Action {
    companion object {
        private const val START_POWER = 0.2
    }

    private val intakeMotor = hardwareMap.crservo.get("IntakeMotor")
    private val intakeRotation = hardwareMap.servo.get("IntakeRotation")

    // Implementing Action interface for the actual Intake

    private var isCanceled = false
    private var isStarted = false

    /**
     * Power to set the intake to
     */
    var intakePower: Double = START_POWER
        set(value) {
            require(value in 0.0..1.0) { "Intake power must be between 0 and 1" }
            field = value
        }

    /**
     * Doesn't actually start the intake, use [startAction] or [start] for that
     */
    override fun run(p: TelemetryPacket): Boolean {
        if (!isStarted)
            return false

        if (isCanceled) {
            isCanceled = false
            isStarted = false

            intakeMotor.power = 0.0
            intakePower = START_POWER
            return false
        }

        intakeMotor.power = intakePower
        return true
    }

    fun cancel() {
        isCanceled = true
    }

    val cancelAction = actionWrapper { cancel() }

    fun start() {
        isStarted = true
    }

    val startAction = actionWrapper { start() }

    fun setPowerAction(power: Double): Action = actionWrapper { this.intakePower = power }

    interface IntakeRotateAction : Action {
        var position: Double
        /**
         * Wraps [position] set in an [Action]
         */
        fun setPositionAction(position: Double): Action
        fun cancel()
    }

    val rotate = object : IntakeRotateAction {
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
}