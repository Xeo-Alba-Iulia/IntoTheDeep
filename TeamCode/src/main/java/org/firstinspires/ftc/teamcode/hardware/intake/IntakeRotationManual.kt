package org.firstinspires.ftc.teamcode.hardware.intake

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.ManualPositionMechanism

class IntakeRotationManual(hardwareMap: HardwareMap) : ManualPositionMechanism {
    private val intakeRotation: Servo = hardwareMap.servo.get("IntakeRotation")

    private var isCanceled = false
    override var targetPosition = 0.0
        set(value) {
            require(value in 0.0..1.0) { "Intake rotation position must be between 0 and 1" }
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