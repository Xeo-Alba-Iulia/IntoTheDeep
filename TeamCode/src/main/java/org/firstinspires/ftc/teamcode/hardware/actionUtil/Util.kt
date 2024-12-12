package org.firstinspires.ftc.teamcode.hardware.actionUtil

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action


internal fun actionWrapper(block: () -> Unit): Action = object : Action {
    override fun run(p: TelemetryPacket): Boolean {
        block()
        return false
    }
}