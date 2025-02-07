package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Extend(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(0.0) {
    override val servos = arrayOf(hardwareMap.servo["Extend1"], hardwareMap.servo["Extend2"])

    init {
        servos.forEach { it.direction = Servo.Direction.REVERSE }
    }
}
