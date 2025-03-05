package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class Sensor(
    hardwareMap: HardwareMap,
) {
    private val sensor: RevColorSensorV3 = hardwareMap.get(RevColorSensorV3::class.java, "sensor_color")

    val isHoldingSample
        get() = sensor.getDistance(DistanceUnit.CM) < 1.1

    val isHoveringSample
        get() = sensor.getDistance(DistanceUnit.CM) < 3.0

    val isRed
        get() = sensor.red() > 0.1 && sensor.green() + sensor.blue() < 0.1

    val isBlue
        get() = sensor.blue() > 0.1 && sensor.red() + sensor.green() < 0.1

    val isYellow
        get() = sensor.red() > 0.1 && sensor.green() > 0.1 && sensor.blue() < 0.1
}
