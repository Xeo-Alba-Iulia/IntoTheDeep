package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap

open class LiftMotors : RequiredDevices {
    override val devicesRequired = listOf(
        "MotorLiftLeft",
        "MotorLiftRight"
    )
}

interface LiftHardwareInterface: MotorHardwareInterface {
    fun lift(gamepad: Gamepad)
}

enum class LiftPosition(val position: Int) {
    UP(0),
    DOWN(1000)
}

open class LiftHardware(hardwareMap: HardwareMap): LiftHardwareInterface {
    companion object : LiftMotors()

    val liftLeft: DcMotorEx
    val liftRight: DcMotorEx

    val lift: Array<DcMotorEx>

    init {
        val deviceIterator = devicesRequired.iterator()

        liftLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        liftRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())

        lift = arrayOf(liftLeft, liftRight)

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        liftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun setMode(mode: DcMotor.RunMode) {
        lift.forEach { it.mode = mode }
    }

    override fun setPower(power: Double) {
        lift.forEach { it.power = power }
    }

    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        lift.forEach { it.zeroPowerBehavior = behavior }
    }

    override fun lift(gamepad: Gamepad) {
        val y: Double = gamepad.right_stick_y.toDouble()

        liftLeft.power = y
        liftRight.power = y
    }
}