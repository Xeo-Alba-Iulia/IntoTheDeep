package org.firstinspires.ftc.teamcode.util

import kotlin.reflect.KProperty

class SinglePress(
    private val getState: () -> Boolean,
) {
    private var lastState = false

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Boolean {
        val currentState = getState()

        val returnValue = currentState && !lastState
        lastState = currentState

        return returnValue
    }
}
