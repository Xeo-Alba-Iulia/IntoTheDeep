package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.pendul.PendulManual

@Config
class PendulTest(hardwareMap: HardwareMap) : TestPosition(PendulManual(hardwareMap)) {
    companion object {
        @JvmField
        var CURRENT_POSITION: Double = 0.0
    }
    override val componentName = "Pendul"

    override fun run(p: TelemetryPacket): Boolean {
        position = CURRENT_POSITION
        return super.run(p)
    }
}