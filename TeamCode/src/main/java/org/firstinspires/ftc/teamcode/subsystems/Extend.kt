package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism

class Extend(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.extend.`in`, true) {
    companion object {
        const val MULTIPLIER = 0.001
    }

    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Extend"])
}