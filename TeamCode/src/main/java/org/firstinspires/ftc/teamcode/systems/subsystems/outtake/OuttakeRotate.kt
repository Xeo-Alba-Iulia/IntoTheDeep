package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions.OuttakeRotate.Companion.down
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import javax.inject.Inject

class OuttakeRotate
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) : ServoPositionMechanism(down) {
        override val servos = arrayOf(hardwareMap.servo["OuttakeRotate"])
    }

@TeleOp
class OuttakeRotateTest : ManualMechanismTeleOp(::OuttakeRotate)
