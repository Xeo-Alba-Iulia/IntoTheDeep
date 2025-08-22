package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import javax.inject.Inject

class IntakeClawRotate
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) : ServoPositionMechanism(Positions.IntakeClawRotate.middle) {
        override val servos = arrayOf(hardwareMap.servo["IntakeClawRotate"])

        override val adjustMultiplier = 0.005
    }
