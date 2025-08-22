package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import javax.inject.Inject

class Sensor
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) {
//    private val sensor: RevColorSensorV3 = hardwareMap.get(RevColorSensorV3::class.java, "sensor_color")
        private val servoSensor: AnalogInput = hardwareMap.analogInput["servo_sensor"]

        /**
         * **Trebuie neapărat verificat după delay-ul de închidere la gheară**
         */
        val isHoldingSample
            get() = servoSensor.voltage >= 1.270

        val isHoveringSample
            get() = 0

        val isRed
            get() = false

        val isBlue
            get() = false

        val isYellow
            get() = false
    }
