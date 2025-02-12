package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions.IntakeClaw.Companion.closed
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions.IntakeClaw.Companion.open
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Claw(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(open) {
    override val servos = arrayOf(hardwareMap.servo["IntakeClaw"])

    var isClosed
        get() = targetPosition >= closed
        set(value) {
            targetPosition = if (value) closed else open
        }
}
