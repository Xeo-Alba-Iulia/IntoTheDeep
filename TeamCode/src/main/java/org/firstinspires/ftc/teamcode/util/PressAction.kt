package org.firstinspires.ftc.teamcode.util

class PressAction(
    val getState: () -> Boolean,
    val action: () -> Unit,
) {
    private var lastState = false

    fun run() {
        val currentState = getState()
        if (currentState && !lastState) {
            action()
        }
        lastState = currentState
    }
}
