package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

open class Pendul(hardwareMap: HardwareMap) {
    private val servoLeft = hardwareMap.servo.get("PendulLeft")
    private val servoRight = hardwareMap.servo.get("PendulRight")

    init {
       servoLeft.direction = Servo.Direction.REVERSE
    }

    protected val servos = arrayListOf(servoLeft, servoRight)

    enum class PendulPosition(val value: Double) {
        DOWN(0.0),
        HALF(0.5),
        UP(0.0)
    }

    var position = PendulPosition.DOWN
        set(value) {
            servos.forEach { it.position = value.value }
            field = value
        }
}

class PendulTesting(hardwareMap: HardwareMap) : Pendul(hardwareMap) {
    fun test(position: Double) {
        servos.forEach { it.position = position }
    }
}