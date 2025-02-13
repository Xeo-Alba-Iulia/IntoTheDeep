package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class IntakePendul(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(0.5) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakePendul"])
}
