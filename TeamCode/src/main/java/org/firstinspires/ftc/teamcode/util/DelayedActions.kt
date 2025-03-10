package org.firstinspires.ftc.teamcode.util

/**
 * A class for running actions after a delay.
 */
class DelayedActions {
    private val actions: MutableList<Pair<Double,() -> Unit>> = mutableListOf()
    /**
     * Adds an action to be run after a delay.
     * @param delay the delay in seconds.
     * @param action the action to run.
     */
    fun addDelayed(delay: Double, action: () -> Unit) {
        actions.add(Pair(delay * 1000 + System.currentTimeMillis(), action))
    }
    /**
     * Runs all actions that have passed their delay.
     */
    fun run() {
        actions.forEach {
            if (it.first <= System.currentTimeMillis()) {
                it.second
                actions.remove(it)
            }
        }
    }
    /**
     * Clears all actions.
     * @see addDelayed
     * @noinspection UnusedDeclaration
     */
    fun clear() {
        actions.clear()
    }
}
