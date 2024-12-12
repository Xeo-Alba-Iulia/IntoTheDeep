package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

@Config
class PendulServoPosition(hardwareMap: HardwareMap) : ServoPosition(
    hardwareMap.servo.get("PendulLeft"),
    hardwareMap.servo.get("PendulRight")
) {
    override val componentName = "Pendul"
    init {
        servos[0].direction = Servo.Direction.REVERSE
    }
}