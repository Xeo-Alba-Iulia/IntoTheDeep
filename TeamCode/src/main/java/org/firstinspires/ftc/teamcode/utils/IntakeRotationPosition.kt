package org.firstinspires.ftc.teamcode.utils

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class IntakeRotationPosition(hardwareMap: HardwareMap) : ServoPosition(
    hardwareMap.servo.get("IntakeRotation")
) {
    override val componentName = "Intake Rotation"
}