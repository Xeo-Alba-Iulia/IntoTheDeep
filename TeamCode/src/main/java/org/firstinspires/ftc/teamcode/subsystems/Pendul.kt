package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap

class Pendul(hardwareMap: HardwareMap) : ManualPositionMechanism {
    private var isCanceled = false

    private val servos = arrayOf(hardwareMap.servo.get("Pendul"))

    override fun cancel() {
        isCanceled = true
    }

    override var targetPosition = 0.0

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false
            targetPosition = 0.0
            return false
        }

        servos.forEach { it.position = targetPosition }

        return true
    }
}