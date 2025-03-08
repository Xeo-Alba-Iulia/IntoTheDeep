package org.firstinspires.ftc.teamcode.nextftc.subsystems

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.tuning.LinearSlideFeedforwardTuner

@TeleOp(name = "Lift Feedforward Tuner")
class LiftTest : NextFTCOpMode(Lift) {
    override fun onStartButtonPressed() {
        val liftLeft = hardwareMap["LiftLeft"] as DcMotorEx
        val liftRight = hardwareMap["LiftRight"] as DcMotorEx
        liftRight.direction = DcMotorSimple.Direction.REVERSE

        LinearSlideFeedforwardTuner(
            listOf(
                liftLeft,
                liftRight,
            ),
            0,
            FtcDashboard.getInstance().telemetry,
        )()
    }
}
