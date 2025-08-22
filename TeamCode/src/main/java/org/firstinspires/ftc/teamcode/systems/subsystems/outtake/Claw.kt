package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions.Claw.Companion.close
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions.Claw.Companion.open
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import javax.inject.Inject

class Claw
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) : ServoPositionMechanism(close) {
        override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Claw"])

        init {
            servos[0].direction = Servo.Direction.REVERSE
        }

        var isClosed
            get() = targetPosition == close
            set(value) {
                targetPosition = if (value) close else open
            }
    }

@TeleOp(name = "Claw test", group = "C")
class ClawTest : ManualMechanismTeleOp(::Claw)
