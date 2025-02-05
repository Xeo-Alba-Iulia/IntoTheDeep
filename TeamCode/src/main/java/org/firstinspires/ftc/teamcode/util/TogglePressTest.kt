package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul

@TeleOp(name = "Toggle Press Test")
class TogglePressTest : OpMode() {
    lateinit var pendul: Pendul
    val pendulUp by TogglePress(this.gamepad1::cross) // Funcție de toggle press

    override fun init() {
        pendul = Pendul(hardwareMap)
    }

    override fun loop() {
        pendul.targetPosition =
            when (pendulUp) {
                true -> 1.0
                false -> 0.0
            }
        pendul.run(TelemetryPacket())
    }
}
