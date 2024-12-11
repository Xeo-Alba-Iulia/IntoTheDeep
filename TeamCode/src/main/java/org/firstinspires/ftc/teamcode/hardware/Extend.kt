package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

open class Extend(hardwareMap: HardwareMap) {
    private val extendLeft = hardwareMap.servo.get("ExtendLeft")
    private val extendRight = hardwareMap.servo.get("ExtendRight")

    init {
        extendLeft.direction = Servo.Direction.REVERSE
    }

    protected val servos = arrayListOf(extendLeft, extendRight)

    enum class ExtendPosition(val position: Double) {
        DOWN(0.0),
        HALF(0.5),
        UP(1.0)
    }

    var position: ExtendPosition? = null
        set(value) {
            require(value != null) { "Extend position must not be null" }
            servos.forEach { it.position = value.position }
            field = value
        }
}

class ExtendTesting(hardwareMap: HardwareMap) : Extend(hardwareMap) {
    fun test(position: Double) {
        servos.forEach { it.position = position }
    }
}