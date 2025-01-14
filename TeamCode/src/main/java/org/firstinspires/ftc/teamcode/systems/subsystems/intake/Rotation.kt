package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class IntakeRotation(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.IntakeRotation.parallel) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakeRotation"])
}

@TeleOp(name = "Intake rotation test", group = "C")
class IntakeRotationTest : ManualMechanismTeleOp(::IntakeRotation)