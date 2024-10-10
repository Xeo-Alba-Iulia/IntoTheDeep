package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple

interface RequiredDevices {
    val devicesRequired: List<String>
}

interface MotorHardwareInterface {
    var power: Double
    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
    var mode: DcMotor.RunMode

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