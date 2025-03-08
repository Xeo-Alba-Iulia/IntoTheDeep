package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Claw
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.ClawRotate
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul
import kotlin.properties.Delegates

@Disabled
@TeleOp(name = "Outtake Test", group = "B")
class OuttakeTest : LinearOpMode() {
    companion object {
        private const val MULTIPLIER = 0.003
    }

    override fun runOpMode() {
        val claw = Claw(hardwareMap)
        val clawRotate = ClawRotate(hardwareMap)
        val pendul = Pendul(hardwareMap)

        waitForStart()

        var clawPosition: Double by Delegates.vetoable(0.0) { _, _, new -> new in 0.0..1.0 }
        var clawRotatePosition: Double by Delegates.vetoable(0.0) { _, _, new -> new in 0.0..1.0 }
        var pendulPosition: Double by Delegates.vetoable(0.0) { _, _, new -> new in 0.0..1.0 }

        val dash = FtcDashboard.getInstance()

        while (opModeIsActive()) {
            val packet = TelemetryPacket()

            clawPosition += MULTIPLIER * gamepad2.left_stick_y
            clawRotatePosition += MULTIPLIER * gamepad1.left_stick_y
            pendulPosition += MULTIPLIER * gamepad1.right_stick_y

            claw.targetPosition = clawPosition
            clawRotate.targetPosition = clawRotatePosition
            pendul.targetPosition = pendulPosition

            claw.run(packet)
            clawRotate.run(packet)
            pendul.run(packet)

            dash.sendTelemetryPacket(packet)
        }
    }
}
