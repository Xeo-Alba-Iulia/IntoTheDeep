package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism

class Claw(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.Claw.open) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Claw"])
}

@TeleOp(group = "Servo Subsystems")
class ClawTest : ManualMechanismTeleOp(::Claw)