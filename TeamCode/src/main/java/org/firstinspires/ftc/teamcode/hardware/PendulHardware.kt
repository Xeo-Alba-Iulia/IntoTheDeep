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
    DOWN(-30),
    HALF(-87),
    UP(-214)
}

class PendulHardware(hardwareMap: HardwareMap): PendulHardwareInterface {
    companion object : PendulMotors()

    val pendulLeft: DcMotorEx
    val pendulRight: DcMotorEx

    val pendul: Array<DcMotorEx>

    val currentPosition: Int
        get() = pendul.map { it.currentPosition }.average().toInt()

    /**
     * Target position for all pendul motors
     *
     * When read returns null if target positions are different and the target position if they are the same
     *
     * When set sets the target position for all pendul motors
     *
     * @see DcMotorEx.getTargetPosition
     * @see DcMotorEx.setTargetPosition
     */
    private var targetPosition: Int?
        get() =
            if (pendul.all { it.targetPosition == pendul.first().targetPosition }) {
                pendul.first().targetPosition
            } else {
                null
            }
        set(value) {
            if (value != null) pendul.forEach { it.targetPosition = value }
        }

    init {
        val deviceIterator = devicesRequired.iterator()

        pendulLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        pendulRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())

        pendul = arrayOf(pendulLeft, pendulRight)

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
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


    override fun pendul(gamepad: Gamepad) {
        TODO("Doar setPendul ar trebui sa mearga")
//        val x: Double = gamepad.right_stick_x.toDouble()
//
//        pendulLeft.power = x / 2
//        pendulRight.power = x / 2
    }

    override fun setPendul(pos: PendulPosition) {
        val position = pos.position

        setPower(0.2)
        targetPosition = position
        setMode(DcMotor.RunMode.RUN_TO_POSITION)
    }
}