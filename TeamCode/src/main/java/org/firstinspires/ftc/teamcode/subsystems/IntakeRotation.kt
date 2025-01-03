package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

class IntakeRotation(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.intakeRotate.parallel) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakeRotation"])
}

@TeleOp(group = "Servo Subsystems")
class IntakeRotationTest : ManualMechanismTeleOp(::IntakeRotation)