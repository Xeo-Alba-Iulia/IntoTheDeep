package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.PendulTesting
import kotlin.properties.Delegates

@TeleOp
class PendulTest : LinearOpMode() {
    override fun runOpMode() {
        val pendul = PendulTesting(hardwareMap)

        var position: Double by Delegates.vetoable(0.5) { _, _, new -> new in 0.0..1.0 }

        val dash = FtcDashboard.getInstance()

        waitForStart()

        while (opModeIsActive()) {
            val pack = TelemetryPacket()
            position += 0.001 * gamepad1.left_stick_y
            pendul.test(position)
            pack.put("Position", position)

            dash.sendTelemetryPacket(pack)
        }
    }
}