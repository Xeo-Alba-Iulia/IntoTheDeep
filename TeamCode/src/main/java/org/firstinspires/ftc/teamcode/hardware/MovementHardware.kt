package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.abs
import kotlin.math.max

open class DefaultMotors : RequiredDevices {
    override val devicesRequired = listOf(
        "MotorFrontLeft",
        "MotorFrontRight",
        "MotorBackLeft",
        "MotorBackRight"
    )
}

interface MovementHardwareInterface: MotorHardwareInterface {
    fun move(gamepad: Gamepad)
}

/**
 * A class that represents the hardware for the movement of the robot
 *
 * @param hardwareMap The hardware map from the OpMode
 */
open class MovementHardware(hardwareMap: HardwareMap): MovementHardwareInterface {
    companion object : DefaultMotors()

    val frontLeft: DcMotorEx
    val frontRight: DcMotorEx
    val backLeft: DcMotorEx
    val backRight: DcMotorEx

    val motors: Array<DcMotorEx>

    init {
        val deviceIterator = devicesRequired.iterator()

        frontLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        frontRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        backLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        backRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())

        motors = arrayOf(frontLeft, frontRight, backLeft, backRight)

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT)
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        arrayOf(frontLeft, backLeft).forEach { it.direction = DcMotorSimple.Direction.REVERSE }
    }

    override fun setMode(mode: DcMotor.RunMode) {
        motors.forEach { it.mode = mode }
    }

    override fun setPower(power: Double) {
        motors.forEach { it.power = power }
    }

    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        motors.forEach { it.zeroPowerBehavior = behavior }
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
}