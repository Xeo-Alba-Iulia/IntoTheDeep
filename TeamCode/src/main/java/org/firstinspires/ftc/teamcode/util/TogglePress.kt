package org.firstinspires.ftc.teamcode.util

import kotlin.reflect.KProperty

/**
 * Delegat pentru butoane de toggle la apăsare.
 *
 * Cand se apasă butonul orice citire va returna true până la următoarea apăsare.
 *
 * @property getState funcție ce returnează starea butonului
 *
 * @sample org.firstinspires.ftc.teamcode.util.TogglePressTest.pendulUp
 */
class TogglePress(
    getState: () -> Boolean,
) {
    val singlePressValue by SinglePress(getState)
    private var state = false

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): Boolean {
        // singlePressValue va fi true doar la prima citire după apăsare și
        // nu e afectat de release
        state = singlePressValue xor state
        return state
    }
}
