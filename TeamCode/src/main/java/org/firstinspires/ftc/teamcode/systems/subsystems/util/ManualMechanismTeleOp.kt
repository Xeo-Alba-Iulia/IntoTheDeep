package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.OpMode

private const val MULTIPLIER = 0.003

abstract class ManualMechanismTeleOp(private val factory: ManualPositionFactory) : OpMode() {
    protected lateinit var manualPositionMechanism: ManualPositionMechanism
    val dashboard = FtcDashboard.getInstance() as FtcDashboard

    override fun init() {
        manualPositionMechanism = factory.manualPositionFactory(hardwareMap)
    }

    override fun loop() {
        manualPositionMechanism.targetPosition -= MULTIPLIER * gamepad1.left_stick_y

        val packet = TelemetryPacket()

        if (manualPositionMechanism.run(packet) == false) {
            requestOpModeStop()
        }

        dashboard.sendTelemetryPacket(packet)
        dashboard.telemetry.update()

        telemetry.addData(
            "Position for ${manualPositionMechanism::class.simpleName}",
            manualPositionMechanism.targetPosition
        )

        telemetry.update()
    }
}