package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.pendul.PendulManual

@Config
class PendulTest(hardwareMap: HardwareMap) : TestPosition(PendulManual(hardwareMap)) {
    override val componentName = "Pendul"
}