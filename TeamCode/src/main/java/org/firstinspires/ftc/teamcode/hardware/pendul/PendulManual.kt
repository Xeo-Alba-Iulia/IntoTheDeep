package org.firstinspires.ftc.teamcode.hardware.pendul

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardware.ManualPositionMechanism

class PendulManual(hardwareMap: HardwareMap) : ManualPositionMechanism {
    private val servoLeft = hardwareMap.servo.get("PendulLeft")
    private val servoRight = hardwareMap.servo.get("PendulRight")

    private var isCanceled = false

    init {
       servoLeft.direction = Servo.Direction.REVERSE
    }

    private val servos = arrayListOf(servoLeft, servoRight)

    fun cancel() {
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