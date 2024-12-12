package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.CRServo

open class Extend(hardwareMap: HardwareMap) {
    private val extendLeft = hardwareMap.crservo.get("ExtendLeft")
    private val extendRight = hardwareMap.crservo.get("ExtendRight")

    init {
        extendLeft.direction.inverted()
    }

    protected val servos = arrayListOf(extendLeft, extendRight)

    fun setPower(power: Double){
        servos.forEach { it.power = power }
    }
}

class ExtendTesting(hardwareMap: HardwareMap) : Extend(hardwareMap) {
    fun test(position: Double) {
        servos.forEach { it.power = position }
    }
}