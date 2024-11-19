package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Intake

@TeleOp
class IntakeTest : LinearOpMode() {
    override fun runOpMode() {
        val intake = Intake(hardwareMap)

        waitForStart()

        runBlocking(ParallelAction(intake.start(), SequentialAction(
            SleepAction(1.0),
            intake.setPower(0.5),
            SleepAction(1.0),
            intake.stop()
        )))
    }
}