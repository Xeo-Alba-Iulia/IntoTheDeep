package org.firstinspires.ftc.teamcode.util

object ServoSmoothing {
    fun servoSmoothing(currPos: Double, targetPos: Double): Double {
        val smoothedPos = (targetPos * 0.07) + (currPos * 0.93)
        return smoothedPos
    }
}