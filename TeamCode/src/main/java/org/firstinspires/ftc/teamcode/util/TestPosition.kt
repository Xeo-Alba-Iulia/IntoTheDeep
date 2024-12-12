package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import org.firstinspires.ftc.teamcode.hardware.ManualPositionMechanism

/**
 * Clasă pentru a găsi poziții la servo-uri
 *
 * Da log la toate pozițiile servo-urilor pe dashboard
 */
abstract class TestPosition(protected val mechanism: ManualPositionMechanism) : Action {
    abstract val componentName: String

    val dashboard: FtcDashboard = FtcDashboard.getInstance()

    /**
     * Target position of the mechanism
     */
    protected var position
        get() = mechanism.targetPosition
        set(value) {
            mechanism.targetPosition = value
        }

    override fun run(p: TelemetryPacket): Boolean {
        p.put("Position for $componentName", position)
        return mechanism.run(p)
    }
}