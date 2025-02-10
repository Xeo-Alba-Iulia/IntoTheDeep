package org.firstinspires.ftc.teamcode.util

fun servoSmoothing(
    currPos: Double,
    targetPos: Double,
): Double {
    val smoothedPos = (targetPos * 0.5) + (currPos * 0.5)
    return smoothedPos
}
