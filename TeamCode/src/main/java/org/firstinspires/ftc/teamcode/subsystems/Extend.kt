package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Extend(hardwareMap: HardwareMap) : ManualPositionMechanism {
    private val extend = hardwareMap.get("Extend") as Servo

    init {
        extend.direction = Servo.Direction.REVERSE
    }

    var isCanceled = false

    override var targetPosition
        get() = extend.position
        set(value) {
            extend.position = value
        }

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false
            return false
        }

        p.put("Extend Position", targetPosition)

        return true
    }

    override fun cancel() {
        isCanceled = true
    }
}