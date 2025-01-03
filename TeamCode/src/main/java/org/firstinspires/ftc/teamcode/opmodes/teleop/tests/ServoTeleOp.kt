package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystems.IntakeRotation
import org.firstinspires.ftc.teamcode.subsystems.Lift
import org.firstinspires.ftc.teamcode.subsystems.Pendul

/**
 * TeleOp principal pentru testarea simultană a pozițiilor mecanismelor.
 *
 * *Cel mai ușor se dă comment la mecanismele nefolosite si se rulează testul.*
 */
@TeleOp(name = "Position Tests", group = "Tests")
class PositionsTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val dashboard = FtcDashboard.getInstance()

        waitForStart()

        /**
         * Lista cu toate mecanismele care vor fi testate.
         */
        // TODO: Se poate obține întreaga lista de mecanisme prin reflection.
        // FIXME: Adaugă mecanismele noi aici
        val servoPositions = listOf(
            IntakeRotation(hardwareMap),
            Pendul(hardwareMap),
            Lift(hardwareMap)
        )

        while (opModeIsActive()) {
            val telemetryPacket = TelemetryPacket()
            servoPositions.forEach {
                it.run(telemetryPacket)
            }

            dashboard.sendTelemetryPacket(telemetryPacket)
            dashboard.telemetry.update()
        }
    }
}