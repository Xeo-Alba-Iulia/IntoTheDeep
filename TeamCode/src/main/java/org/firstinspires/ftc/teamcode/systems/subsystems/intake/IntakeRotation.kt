package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions

class IntakeRotation(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.IntakeRotation.parallel) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakeRotation"])
}

@TeleOp(group = "Servo Subsystems")
class IntakeRotationTest : ManualMechanismTeleOp(::IntakeRotation)