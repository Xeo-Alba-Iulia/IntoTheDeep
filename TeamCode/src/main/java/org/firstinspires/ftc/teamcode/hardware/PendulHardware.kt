package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap

open class PendulMotors : RequiredDevices {
    override val devicesRequired = listOf(
        "MotorPendulLeft",
        "MotorPendulRight"
    )
}

interface PendulHardwareInterface: MotorHardwareInterface {
    fun pendul(gamepad: Gamepad)
}

open class PendulHardware(hardwareMap: HardwareMap): PendulHardwareInterface {
    companion object : PendulMotors()

    val pendulLeft: DcMotorEx
    val pendulRight: DcMotorEx

    val pendul: Array<DcMotorEx>

    init {
        val deviceIterator = devicesRequired.iterator()

        pendulLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        pendulRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())

        pendul = arrayOf(pendulLeft, pendulRight)

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        pendulRight.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun setMode(mode: DcMotor.RunMode) {
        pendul.forEach { it.mode = mode }
    }

    override fun setPower(power: Double) {
        pendul.forEach { it.power = power }
    }

    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        pendul.forEach { it.zeroPowerBehavior = behavior }
    }

    override fun pendul(gamepad: Gamepad) {
        val x: Double = gamepad.right_stick_x.toDouble()

        pendulLeft.power = x
        pendulRight.power = x
    }
}