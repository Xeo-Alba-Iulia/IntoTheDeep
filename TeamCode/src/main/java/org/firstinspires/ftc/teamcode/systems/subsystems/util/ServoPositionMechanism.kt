package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range

abstract class ServoPositionMechanism(
    initialPosition: Double = 0.0,
) : ManualPositionMechanism {
    protected abstract val servos: Array<Servo>

    private var isCanceled = false
    override var targetPosition = initialPosition
        set(value) {
            field = Range.clip(value, 0.0, 1.0)
        }

    override fun cancel() {
        isCanceled = false
    }

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false

            return false
        }

        servos.forEach { it.position = targetPosition }

        p.put("Position for ${this::class.simpleName}", targetPosition)

        return true
    }
}
