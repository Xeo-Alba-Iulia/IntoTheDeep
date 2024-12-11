package org.firstinspires.ftc.teamcode.utils

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range.clip

/**
 * Clasă pentru a găsi poziții la servo-uri
 *
 * Da log la toate pozițiile servo-urilor pe dashboard
 * @property servos Lista de servo-uri
 */
abstract class ServoPosition(protected val servos: List<Servo>) {
    companion object {
        @Volatile @JvmField var CURRENT_POSITION = 0.0
    }

    val dashboard: FtcDashboard = FtcDashboard.getInstance()

    // Vararg Constructor
    constructor(vararg servos: Servo): this(servos.toList())

    /**
     * Target position of the servos, all values are clipped to [0, 1]
     */
    var position: Double = 0.0
        set(value) {
            field = clip(value, 0.0, 1.0)
            servos.forEach { it.position = field }
            dashboard.telemetry.addData("Servo Position", field)
        }

    init {
        update()
    }

    /**
     * Update the position of the servos, according to FtcDashboard
     */
    fun update() {
        position = CURRENT_POSITION
    }
}