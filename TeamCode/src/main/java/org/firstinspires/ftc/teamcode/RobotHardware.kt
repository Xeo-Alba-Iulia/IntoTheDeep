package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.MovementHardware

class RobotHardware(hardwareMap: HardwareMap) : MovementHardware(hardwareMap) {
    @Deprecated("Use move instead", ReplaceWith("move(gamepad)"))
    public override fun movement(gamepad: Gamepad) {
        super.movement(gamepad)
    }
}