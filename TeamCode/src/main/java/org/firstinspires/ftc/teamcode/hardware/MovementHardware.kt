package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.abs
import kotlin.math.max

open class DefaultDevices : RequiredDevices {
    override val devicesRequired = listOf(
        "MotorFrontLeft",
        "MotorFrontRight",
        "MotorBackLeft",
        "MotorBackRight"
    )
}

interface MovementHardwareInterface {
    fun move(gamepad: Gamepad)
}

/**
 * A class that represents the hardware for the movement of the robot
 *
 * @param hardwareMap The hardware map from the OpMode
 * @throws HardwareNotFoundException If one or more motors are null
 */
open class MovementHardware
@Throws(HardwareNotFoundException::class)
constructor(hardwareMap: HardwareMap): MovementHardwareInterface, MotorHardwareInterface {
    companion object : DefaultDevices()

    lateinit var frontLeft: DcMotorEx
    lateinit var frontRight: DcMotorEx
    lateinit var backLeft: DcMotorEx
    lateinit var backRight: DcMotorEx

    override val motors = arrayListOf(frontLeft, frontRight, backLeft, backRight)

    init {
        val exception = HardwareNotFoundException()

        val deviceIterator = devicesRequired.iterator()
        val motorIterator = motors.listIterator()

        while (motorIterator.hasNext()) {
            val cur = deviceIterator.next()
            motorIterator.next()
            motorIterator.set((hardwareMap.get(cur) ?: run {
                exception.add(cur)
                null
            }) as DcMotorEx)
        }
        if (exception.isNotEmpty)
            throw exception

        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        arrayOf(frontLeft, backLeft).forEach { it.direction = DcMotorSimple.Direction.REVERSE }
    }

    /**
     * Move the robot based on the gamepad input
     *
     * @param gamepad The gamepad to get input from
     */
    override fun move(gamepad: Gamepad) {
        val y = -gamepad.left_stick_y
        val x = gamepad.left_stick_x * 1.1
        val rx = gamepad.right_stick_x

        val denominator = max(1.0, abs(y) + abs(x) + abs(rx))

        frontLeft.power = (y + x + rx) / denominator
        frontRight.power = (y - x - rx) / denominator
        backLeft.power = (y - x + rx) / denominator
        backRight.power = (y + x - rx) / denominator
    }

    @Deprecated("Use move instead", ReplaceWith("move(gamepad)"))
    protected open fun movement(gamepad: Gamepad) = move(gamepad)
}