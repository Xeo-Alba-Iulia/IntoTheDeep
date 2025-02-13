package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Extend(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.Extend.`in`) {
    override val servos = arrayOf(hardwareMap.servo["Extend1"], hardwareMap.servo["Extend2"])
}
