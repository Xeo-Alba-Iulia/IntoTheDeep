package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

/**
 * Clasa pentru rotirea fata de axa paralele cu pamantul a intake-ului
 */
class IntakeRotate(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism() {
    override val servos = arrayOf(hardwareMap.servo["IntakeRotate"])
}
