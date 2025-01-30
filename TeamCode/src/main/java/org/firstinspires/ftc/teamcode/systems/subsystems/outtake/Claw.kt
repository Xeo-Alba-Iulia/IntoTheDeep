package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Claw(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.Claw.close) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Claw"])

    init {
        targetPosition = Positions.Claw.open
        servos[0].direction = Servo.Direction.REVERSE
    }
}

@TeleOp(name = "Claw test", group = "C")
class ClawTest : ManualMechanismTeleOp(::Claw)
