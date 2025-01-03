package org.firstinspires.ftc.teamcode.subsystems.util

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.Servo

abstract class ServoPositionMechanism(
    initialPosition: Double,
) : ManualPositionMechanism {
    protected abstract val servos: Array<Servo>

    private var isCanceled = false
    override var targetPosition = initialPosition

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