package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.abs
import kotlin.math.max

/**
 * A class that represents the hardware for the movement of the robot
 *
 * @param hardwareMap The hardware map from the OpMode
 * @throws NullPointerException If one or more motors are null
 */
open class MovementHardware @Throws(NullPointerException::class) constructor(hardwareMap: HardwareMap) {
    val frontLeft = hardwareMap.get(DcMotorEx::class.java, "MotorFrontLeft")
    val frontRight = hardwareMap.get(DcMotorEx::class.java, "MotorFrontRight")
    val backLeft = hardwareMap.get(DcMotorEx::class.java, "MotorBackLeft")
    val backRight = hardwareMap.get(DcMotorEx::class.java, "MotorBackRight")

    val motors = arrayOf(frontLeft, frontRight, backLeft, backRight)

    init {
        if (motors.any { it == null }) {
            throw NullPointerException("""
                One or more motors are null.
                Make sure that the names of the motors in the code match the names of the motors in the configuration.
            """.trimIndent())
        }

        for (motor in motors) {
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        arrayOf(frontLeft, backLeft).forEach { it.direction = DcMotorSimple.Direction.REVERSE }
    }

    /**
     * Move the robot based on the gamepad input
     *
     * @param gamepad The gamepad to get input from
     */
    fun move(gamepad: Gamepad) {
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