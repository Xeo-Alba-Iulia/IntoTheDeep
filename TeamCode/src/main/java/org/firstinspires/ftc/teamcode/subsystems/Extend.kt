package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism

private const val MAX_POSITION = 0.9

class Extend(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.Extend.`in`) {
    companion object {
        const val MULTIPLIER = 0.001
    }

    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Extend"])

    override var targetPosition: Double
        get() = super.targetPosition
        set(value) {
            super.targetPosition = Range.clip(value, 0.0, MAX_POSITION)
        }
}

//@TeleOp(group = "Servo Subsystems")
//class ExtendTest : ManualMechanismTeleOp(::Extend)