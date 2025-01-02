package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.subsystems.util.ServoPositionMechanism

class IntakeRotation(
    hardwareMap: HardwareMap,
    isVerbose: Boolean = false
) : ServoPositionMechanism(Positions.intakeRotate.parallel, isVerbose) {
    override val servos: Array<Servo> = arrayOf(hardwareMap.servo["IntakeRotation"])
}