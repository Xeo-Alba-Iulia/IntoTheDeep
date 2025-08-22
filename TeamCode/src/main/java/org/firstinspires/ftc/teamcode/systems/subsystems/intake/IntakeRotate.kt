package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import javax.inject.Inject

/**
 * Clasa pentru rotirea fata de axa paralele cu pamantul a intake-ului
 */
class IntakeRotate
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) : ServoPositionMechanism(Positions.IntakeRotate.transfer) {
        override val servos = arrayOf(hardwareMap.servo["IntakeRotate"])

        override val adjustMultiplier = 0.005
    }
