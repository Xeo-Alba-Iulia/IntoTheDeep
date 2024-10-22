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
    fun setPendul(pos: PendulPosition)
}

enum class PendulPosition(val position: Int) {
    DOWN(0),
    HALF(100),
    UP(200)
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
        setMode(DcMotor.RunMode.RUN_USING_ENCODER)

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

    private var targetPosition: Int?
        get() =
            if (pendul.all { it.targetPosition == pendul[0].targetPosition }) {
                pendul[0].targetPosition
            } else {
                null
            }
        set(value) {
            pendul.forEach { it.targetPosition = value!! }
        }


    override fun pendul(gamepad: Gamepad) {
        val x: Double = gamepad.right_stick_x.toDouble()

        pendulLeft.power = x / 2
        pendulRight.power = x / 2
    }

    override fun setPendul(pos: PendulPosition) {
        val position: Int = pos.position

        setPower(0.25)
        targetPosition = position
    }
}