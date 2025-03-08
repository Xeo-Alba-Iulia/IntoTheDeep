package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.AnalogInput
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

object Sensor : Subsystem() {
    private lateinit var sensor: RevColorSensorV3
    private lateinit var servoSensor: AnalogInput

    override fun initialize() {
        sensor = OpModeData.hardwareMap.get(RevColorSensorV3::class.java, "sensor_color")
        servoSensor = OpModeData.hardwareMap.analogInput["servo_sensor"]
    }

    val isHoldingSample
        get() = IntakeClaw.isClosed && servoSensor.voltage >= 1.273

    val isHoveringSample
        get() = sensor.getDistance(DistanceUnit.CM) < 5.0

    val isRed
        get() = sensor.red() > 0.1 && sensor.green() + sensor.blue() < 0.1

    val isBlue
        get() = sensor.blue() > 0.1 && sensor.red() + sensor.green() < 0.1

    val isYellow
        get() = sensor.red() > 0.1 && sensor.green() > 0.1 && sensor.blue() < 0.1

    val voltage get() = servoSensor.voltage

    fun red() = sensor.red()

    fun green() = sensor.green()

    fun blue() = sensor.blue()
}
