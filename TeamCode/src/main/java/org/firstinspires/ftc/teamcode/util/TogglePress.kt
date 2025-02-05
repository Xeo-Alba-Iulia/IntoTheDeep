package org.firstinspires.ftc.teamcode.util

import kotlin.reflect.KProperty

class TogglePress(
    getState: () -> Boolean,
) {
    val singlePressValue by SinglePress(getState)
    private var state = false

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Boolean {
        state = singlePressValue xor state
        return state
    }
}
