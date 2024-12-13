package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.intake.IntakeRotationManual

@Config
class IntakeRotationTest(hardwareMap: HardwareMap) : TestPosition(IntakeRotationManual(hardwareMap)) {
    companion object {
        @JvmField
        @Volatile
        var CURRENT_POSITION: Double = 0.0
    }
    override val componentName = "Intake Rotation"

    override fun run(p: TelemetryPacket): Boolean {
        position = CURRENT_POSITION
        return super.run(p)
    }
}