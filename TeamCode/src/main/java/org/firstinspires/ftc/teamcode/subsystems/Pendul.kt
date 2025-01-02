package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism

class Pendul(hardwareMap: HardwareMap) : ServoPositionMechanism(Positions.pendul.down) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["Pendul"])
}