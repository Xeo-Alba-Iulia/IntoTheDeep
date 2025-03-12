package org.firstinspires.ftc.teamcode.opmodes.auto

import com.pedropathing.localization.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.util.autoPose

@Autonomous
class ResetStoredPose : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()

        autoPose = Pose(0.0, 0.0, 0.0)
        this.requestOpModeStop()
    }
}
