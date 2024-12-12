package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.Intake

@Config
class IntakeRotationTest(hardwareMap: HardwareMap) : TestPosition(Intake(hardwareMap).rotate) {
    override val componentName = "Intake Rotation"
}