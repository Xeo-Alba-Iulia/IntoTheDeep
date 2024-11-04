package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor

/**
 * Interface that represents the required devices for a hardware class
 * The companion object of the class should extend this interface
 *
 * @property devicesRequired The list of devices required for the hardware class
 * @see MovementHardware.Companion
 */
interface RequiredDevices {
    val devicesRequired: List<String>
}

/**
 * Interface for motor hardware operations
 */
interface MotorHardwareInterface {
    /**
     * Sets the same power to all motors
     *
     * @param power The power to set the motors to
     */
    fun setPower(power: Double)

    /**
     * Sets zero power behavior of all motors
     *
     * @param behavior The behavior to set the motors to
     */
    fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior)

    /**
     * Sets the mode of all motors
     *
     * @param mode The mode to set the motors to
     */
    fun setMode(mode: DcMotor.RunMode)
}