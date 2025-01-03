package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystems.Extend
import org.firstinspires.ftc.teamcode.subsystems.util.Positions

@TeleOp
@Config
class ExtendTest : LinearOpMode() {
    companion object {
        @JvmField
        @Volatile
        var currentPosition = 0.0
    }

    override fun runOpMode() {
        val extend = Extend(hardwareMap)

        waitForStart()

        while (opModeIsActive()) {
            currentPosition = when {
                gamepad1.dpad_left -> Positions.extend.`in`
                gamepad1.dpad_down -> Positions.extend.out
                else -> currentPosition - gamepad1.right_stick_y * Extend.MULTIPLIER
            }
            extend.targetPosition = currentPosition
            currentPosition = extend.targetPosition

            val telemetryPacket = TelemetryPacket()
            extend.run(telemetryPacket)
            FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket)
        }
    }

}