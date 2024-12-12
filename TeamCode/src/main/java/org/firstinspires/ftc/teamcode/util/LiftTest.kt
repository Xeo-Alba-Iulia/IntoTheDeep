package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.lift.LiftManual

@Config
class LiftTest(hardwareMap: HardwareMap) : TestPosition(LiftManual(hardwareMap)) {
    override val componentName = "Lift"
}