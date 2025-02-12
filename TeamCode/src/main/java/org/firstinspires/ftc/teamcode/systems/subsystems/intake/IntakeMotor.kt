package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeMotor(
    hardwareMap: HardwareMap,
) : Action {
    var intakePower = 0.0

    override fun run(p: TelemetryPacket) = false
}
