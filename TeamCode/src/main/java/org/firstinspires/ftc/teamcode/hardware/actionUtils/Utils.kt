package org.firstinspires.ftc.teamcode.hardware.actionUtils

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action


internal fun actionWrapper(block: () -> Unit): Action = object : Action {
    override fun run(p: TelemetryPacket): Boolean {
        block()
        return false
    }
}

internal fun actionWrapperTelemetry(block: (TelemetryPacket) -> Unit): Action = object : Action {
    override fun run(p: TelemetryPacket): Boolean {
        block(p)
        return false
    }
}