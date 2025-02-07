package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Extend(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(0.0) {
    override val servos = arrayOf(hardwareMap.servo["Extend1"], hardwareMap.servo["Extend2"])
}

@TeleOp
class ExtendTest : ManualMechanismTeleOp(::Extend)
