package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystems.Extend

private const val MULTIPLIER = 0.001

@TeleOp
class ExtendTest : LinearOpMode() {
    override fun runOpMode() {
        val extend = Extend(hardwareMap)

        waitForStart()

        while (opModeIsActive()) {
            extend.power = gamepad1.right_stick_y * MULTIPLIER
            extend.run(TelemetryPacket())
        }
    }

}