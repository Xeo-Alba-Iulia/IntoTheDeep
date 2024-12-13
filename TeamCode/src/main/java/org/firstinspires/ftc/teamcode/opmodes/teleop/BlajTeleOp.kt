package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.acmerobotics.dashboard.FtcDashboard
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
        actionList.forEach { it.run(telemetryPacket) }
    }

    override fun runOpMode() {
        robotHardware = RobotHardware(hardwareMap)

        actionList = listOf(
            robotHardware.intake,
        )

        val moveGamepad = gamepad1
        val controlGamepad = gamepad2

        val dashboard = FtcDashboard.getInstance()

        waitForStart()

        while(opModeIsActive()) {
            // Movement
            robotHardware.move(moveGamepad)

            // Actions for other hardware (intake, lift, etc)
            val telemetryPacket = TelemetryPacket()
            runActions(telemetryPacket)
            dashboard.sendTelemetryPacket(telemetryPacket)

            // Finite State Machine
            when {

            }
            robotHardware.extend.power = controlGamepad.right_stick_y.toDouble()
        }
    }
}