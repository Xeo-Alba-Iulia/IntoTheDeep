package org.firstinspires.ftc.teamcode.util

import android.util.Log

class ServoSmooth(
    var currentPositionRatio: Double,
) {
    init {
        require(currentPositionRatio in 0.0..1.0)
    }

    var targetPosition = 0.0
        set(value) {
            field = value.coerceIn(0.0..1.0)
        }

    operator fun get(currentPosition: Double): Double {
        val returnValue =
            (currentPosition * currentPositionRatio) + (targetPosition * (1 - currentPositionRatio))

        Log.d("ServoSmooth", "Current position: $currentPosition, return: $returnValue")
        return returnValue
    }
}
