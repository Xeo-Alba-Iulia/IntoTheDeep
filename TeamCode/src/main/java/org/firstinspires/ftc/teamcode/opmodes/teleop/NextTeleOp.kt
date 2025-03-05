package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.follower.Follower
import com.pedropathing.util.Constants
import com.rowanmcalpin.nextftc.pedro.PedroOpMode
import org.firstinspires.ftc.teamcode.nextftc.Intake
import org.firstinspires.ftc.teamcode.nextftc.Outtake
import org.firstinspires.ftc.teamcode.nextftc.subsystems.Lift
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.Extend
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeClaw
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeClawRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakePendul
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.Claw
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.ClawRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.util.PoseStore
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

class NextTeleOp :
    PedroOpMode(
        Extend,
        IntakeClaw,
        IntakeClawRotate,
        IntakePendul,
        IntakeRotate,
        Claw,
        ClawRotate,
        Pendul,
    ) {
    val inTransfer
        get() = listOf(Intake.inTransfer, Outtake.inTransfer, Lift.inTransfer).all { it }

    override fun onInit() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        follower = Follower(hardwareMap)
        follower.setStartingPose(PoseStore.pose)
    }
}
