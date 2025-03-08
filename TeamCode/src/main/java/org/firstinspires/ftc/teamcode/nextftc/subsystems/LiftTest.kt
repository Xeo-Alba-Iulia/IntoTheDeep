package org.firstinspires.ftc.teamcode.nextftc.subsystems

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode

@TeleOp
class LiftTest : NextFTCOpMode(SubsystemGroup(Lift)) {
    override fun onStartButtonPressed() {
        gamepadManager.gamepad1.circle.pressedCommand = { Lift.toHighBar }
        gamepadManager.gamepad1.triangle.pressedCommand = { Lift.toHighBasket }
        gamepadManager.gamepad1.square.pressedCommand = { Lift.toLow }
    }
}
