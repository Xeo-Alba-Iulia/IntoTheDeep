package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

class IntakePendul(hardwareMap: HardwareMap) : ServoPositionMechanism(0.6) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakePendul"])
}

@TeleOp(group = "Servo Subsystems")
class IntakePendulTest : ManualMechanismTeleOp(::IntakePendul)