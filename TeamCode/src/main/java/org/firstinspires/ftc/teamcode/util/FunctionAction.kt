package org.firstinspires.ftc.teamcode.util

/**
 * Data class for executing an action based on the result of check()
 *
 * @param check         The check function for this action
 * @param execute       Function to execute the first time check evaluates to true
 * @param willCancel    Whether the Action should keep executing after it completes once
 * @param lastState     Should be set
 */
open class FunctionAction(
    val check: () -> Boolean,
    val willCancel: Boolean = false,
    val initialState: Boolean = check(),
    val execute: () -> Unit,
) {
    open var isCanceled = false
    private var lastState = initialState

    operator fun invoke() {
        val currentState = check()
        if (currentState && !lastState && !isCanceled) {
            isCanceled = willCancel
            execute()
        }

        lastState = currentState
    }

    open fun invert() = FunctionAction({ !check() }, willCancel, !initialState, execute)

    override fun toString() = "${this::class.simpleName} with willCancel = $willCancel"
}
