package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap

class Extend(hardwareMap: HardwareMap) {
    private val extendLeft = hardwareMap.crservo.get("ExtendLeft")
    private val extendRight = hardwareMap.crservo.get("ExtendRight")

    init {
        extendLeft.direction.inverted()
    }

    val servos = arrayListOf(extendLeft, extendRight)

    var power: Double
        get() = extendLeft.power
        set(value) {
            servos.forEach { it.power = value }
        }
}