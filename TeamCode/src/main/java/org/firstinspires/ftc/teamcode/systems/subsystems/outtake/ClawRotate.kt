package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class ClawRotate(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.ClawRotate.transfer) {
    override val servos = arrayOf(hardwareMap.servo["ClawRotate"])
}

@TeleOp(group = "Servo Subsystems")
class ClawRotateTest : ManualMechanismTeleOp(::ClawRotate)