package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

class Pendul(hardwareMap: HardwareMap) : ServoPositionMechanism(0.66) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Pendul"])

    companion object {
        const val MULTIPLIER = 0.001
    }
}

@TeleOp(group = "Servo Subsystems")
class PendulTest : ManualMechanismTeleOp(::Pendul)