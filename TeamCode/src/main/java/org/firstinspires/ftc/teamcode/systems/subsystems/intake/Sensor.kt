package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class Sensor(
    hardwareMap: HardwareMap,
) {
    private val sensor: RevColorSensorV3 = hardwareMap.get(RevColorSensorV3::class.java, "sensor_color")
    private val servoSensor: AnalogInput = hardwareMap.analogInput["servo_sensor"]

    /**
     * **Trebuie neapărat verificat după delay-ul de închidere la gheară**
     */
    val isHoldingSample
        get() = servoSensor.voltage >= 1.270

    val isHoveringSample
        get() = sensor.getDistance(DistanceUnit.CM) < 4.0

    val isRed
        get() = sensor.red() > sensor.green() && sensor.red() > 250

    val isBlue
        get() = sensor.blue() > 2 * sensor.green()

    val isYellow
        get() = sensor.green() > sensor.red() && sensor.green() > 450 && sensor.blue() < 1000

    fun red() = sensor.red()

    fun green() = sensor.green()

    fun blue() = sensor.blue()
}
