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
    companion object {
        @Volatile @JvmField var CURRENT_POSITION = 0.0
    }

    abstract val componentName: String

    val dashboard: FtcDashboard = FtcDashboard.getInstance()

    /**
     * Target position of the mechanism
     */
    var position = 0.0
        get() = mechanism.targetPosition
        set(value) {
            field = value
            mechanism.targetPosition = field
            dashboard.telemetry.addData("Servo Position for $componentName", field)
        }

    override fun run(p: TelemetryPacket) = mechanism.run(p)

    init {
        updateFromDashboard()
    }

    /**
     * Update the position of the servos, according to FtcDashboard
     */
    fun updateFromDashboard() {
        position = CURRENT_POSITION
    }
}