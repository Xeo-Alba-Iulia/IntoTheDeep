package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Claw(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.Claw.close) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Claw"])
}

@TeleOp(group = "Servo Subsystems")
class ClawTest : ManualMechanismTeleOp(::Claw)