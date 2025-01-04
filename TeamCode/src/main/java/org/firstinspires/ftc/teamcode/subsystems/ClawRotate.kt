package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism

class ClawRotate(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.ClawRotate.outtake) {
    override val servos = arrayOf(hardwareMap.servo["ClawRotate"])
}

@TeleOp(group = "Servo Subsystems")
class ClawRotateTest : ManualMechanismTeleOp(::ClawRotate)