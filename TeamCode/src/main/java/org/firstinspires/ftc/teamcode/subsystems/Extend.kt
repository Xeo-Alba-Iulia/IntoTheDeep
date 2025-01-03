package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

class Extend(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.extend.`in`) {
    companion object {
        const val MULTIPLIER = 0.001
    }

    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Extend"])
}

//@TeleOp(group = "Servo Subsystems")
//class ExtendTest : ManualMechanismTeleOp(::Extend)