package org.firstinspires.ftc.teamcode.util

import kotlin.reflect.KProperty

/**
 * Delegat pentru butoane ce trebuie sa fact o actioned/apăsare.
 *
 * Cand se apasă butonul următoarea citire va returna true, apoi
 * false pana cand butonul este eliberat si apăsat din nou.
 *
 * @property getState funcție ce returnează starea butonului
 */
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
