package org.firstinspires.ftc.teamcode.util

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

    operator fun get(currentPosition: Double) =
        (currentPosition * currentPositionRatio) + (targetPosition * (1 - currentPositionRatio))
}
