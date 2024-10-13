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

/**
 * Exception thrown when a device is not found
 *
 * @property deviceNames The names of the devices that are not found
 */
class HardwareNotFoundException(
    deviceNames: Collection<String> = emptyList()
): Exception() {
    private val deviceNames = deviceNames.toMutableList()

    /**
     * Adds a device to the end of the list
     *
     * @param deviceName The name of the device
     */
    fun add(deviceName: String) = this.deviceNames.add(deviceName)

    /**
     * Adds multiple devices to the list.
     *
     * The devices are added in the order they appear in the [deviceNames] collection
     *
     * @return `true` if the list of device names changed as a result of the call
     */
    fun addAll(deviceNames: Collection<String>) = this.deviceNames.addAll(deviceNames)

    fun isEmpty() = deviceNames.isEmpty()

    override val message: String?
        get() =
            if (deviceNames.isEmpty()) {
                "One or more devices are not found"
            } else {
                "The following devices are not found: ${deviceNames.joinToString(", ")}"
            }

    operator fun plusAssign(deviceNames: Collection<String>) {
        addAll(deviceNames)
    }

    operator fun plusAssign(deviceName: String) {
        add(deviceName)
    }

    operator fun plusAssign(exception: HardwareNotFoundException) {
        this += exception.deviceNames
    }
}