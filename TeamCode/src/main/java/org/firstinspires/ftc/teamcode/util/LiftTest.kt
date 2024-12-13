package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.lift.LiftManual

@Config
class LiftTest(hardwareMap: HardwareMap) : TestPosition(LiftManual(hardwareMap)) {
    companion object {
        @JvmField
        @Volatile
        var CURRENT_POSITION: Double = 0.0
    }
    override val componentName = "Lift"

    override fun run(p: TelemetryPacket): Boolean {
        position = CURRENT_POSITION
        return super.run(p)
    }
}