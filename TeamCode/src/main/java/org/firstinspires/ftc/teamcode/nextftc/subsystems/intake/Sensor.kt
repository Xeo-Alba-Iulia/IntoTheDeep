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
        get() = IntakeClaw.isClosed && servoSensor.voltage >= 1.270

    val isHoveringSample
        get() = sensor.getDistance(DistanceUnit.CM) < 4.0

    val isRed
        get() = sensor.red() > sensor.green() && sensor.red() > 250

    val isBlue
        get() = sensor.blue() > 2 * sensor.green() && sensor.blue() > 250

    val isYellow
        get() = sensor.green() > sensor.red() && sensor.green() > 450 && sensor.blue() < 1000

    val voltage get() = servoSensor.voltage

    fun red() = sensor.red()

    fun green() = sensor.green()

    fun blue() = sensor.blue()
}
