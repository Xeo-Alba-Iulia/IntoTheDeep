package org.firstinspires.ftc.teamcode.subsystems.util

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.ManualPositionMechanism

abstract class ServoPositionMechanism(
    initialPosition: Double,
    private val isVerbose: Boolean = false
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

        if (isVerbose) {
            require(this::class.simpleName != null) { "Anonymous class not supported for verbose logging" }
            p.put("Position for ${this::class.simpleName}", targetPosition)
        }

        return true
    }
}