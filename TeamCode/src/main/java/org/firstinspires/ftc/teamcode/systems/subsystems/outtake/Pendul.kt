package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class Pendul(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.Pendul.pickup) {
    override val servos: Array<Servo> =
        arrayOf(hardwareMap.servo["Pendul1"], hardwareMap.servo["Pendul2"])
}

@TeleOp(name = "Outtake pendul test", group = "C")
class PendulTest : ManualMechanismTeleOp(::Pendul)
