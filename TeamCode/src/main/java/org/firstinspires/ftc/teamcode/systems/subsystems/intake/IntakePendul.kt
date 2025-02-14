package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism

class IntakePendul(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.IntakePendul.transfer) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakePendul"])
}
