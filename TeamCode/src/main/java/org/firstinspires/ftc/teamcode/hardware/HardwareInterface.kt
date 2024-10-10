package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

interface RequiredDevices {
    val devicesRequired: List<String>
}

interface MotorHardwareInterface {
    var power: Double
        get() = TODO()
        set(value) {
            motors.forEach { it.power = value }
        }

    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        get() = TODO()
        set(value) {
            motors.forEach { it.zeroPowerBehavior = value }
        }

    var mode: DcMotor.RunMode
        get() = TODO()
        set(value) {
            motors.forEach { it.mode = value }
        }

    val motors: Collection<DcMotorEx>
}

class HardwareNotFoundException(deviceNames: Collection<String>): Exception() {
    private val deviceNames = deviceNames.toMutableList()

    constructor(): this(mutableListOf())

    fun add(deviceName: String) = this.deviceNames.add(deviceName)
    fun addAll(deviceNames: Collection<String>) = this.deviceNames.addAll(deviceNames)
    val isEmpty
        get() = deviceNames.isEmpty()

    val isNotEmpty
        get() = deviceNames.isNotEmpty()

    override val message: String?
        get() =
            if (deviceNames.isEmpty()) {
                "One or more devices are not found"
            } else {
                "The following devices are not found: ${deviceNames.joinToString(", ")}"
            }
}