package org.firstinspires.ftc.teamcode.util

import android.util.Log

class ServoSmooth(
    val currentPositionRatio: Double,
) {
    var targetPosition = 0.0

    operator fun get(currentPosition: Double): Double {
        val returnValue =
            (currentPosition * currentPositionRatio) + (targetPosition * (1 - currentPositionRatio))

        Log.d("ServoSmooth", "Current position: $currentPosition, return: $returnValue")
        return returnValue
    }
}
