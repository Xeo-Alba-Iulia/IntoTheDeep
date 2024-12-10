package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RobotHardware

@TeleOp
class BlajTeleOp : LinearOpMode() {
    lateinit var robotHardware: RobotHardware

    lateinit var actionList: List<Action>

    internal fun runActions(telemetryPacket: TelemetryPacket) {

    }

    override fun runOpMode() {
        robotHardware = RobotHardware(hardwareMap)

        actionList = listOf(
            robotHardware.intake
        )

        val moveGamepad = gamepad1
        val controlGamepad = gamepad2

        waitForStart()

        while(opModeIsActive()) {
            robotHardware.move(moveGamepad)
        }
    }
}