package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class IntakeClawRotate(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.IntakeClawRotate.middle) {
    override val servos = arrayOf(hardwareMap.servo["IntakeClawRotate"])
}
