package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class IntakePendul(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(0.9) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakePendul"])
}

@TeleOp(name = "Intake pendul test", group = "C")
class IntakePendulTest : ManualMechanismTeleOp(::IntakePendul)
