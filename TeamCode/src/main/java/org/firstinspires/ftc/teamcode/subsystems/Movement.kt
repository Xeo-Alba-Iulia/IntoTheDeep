package org.firstinspires.ftc.teamcode.subsystems

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
 */
open class Movement(hardwareMap: HardwareMap) {
    val frontLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "MotorFrontLeft")
    val frontRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "MotorFrontRight")
    val backLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "MotorBackLeft")
    val backRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "MotorBackRight")

    init {
        listOf(frontLeft, backLeft, frontRight, backRight).forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
            it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        listOf(frontLeft, backLeft).forEach { it.direction = DcMotorSimple.Direction.REVERSE }
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
}