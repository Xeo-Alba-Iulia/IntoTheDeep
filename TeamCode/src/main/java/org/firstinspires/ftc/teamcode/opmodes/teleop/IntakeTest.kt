package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Intake

@TeleOp
class IntakeTest : LinearOpMode() {
    override fun runOpMode() {
        val intake = Intake(hardwareMap)

        waitForStart()

        runBlocking(intake.runUntilCaught)
    }
}