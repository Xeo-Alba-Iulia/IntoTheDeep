package org.firstinspires.ftc.teamcode.util

/**
 * Data class for executing an action based on the result of check()
 *
 * @param check         The check function for this action
 * @param execute       Function to execute the first time check evaluates to true
 * @param willCancel    Whether the Action should keep executing after it completes once
 * @param lastState     Should be set
 */
open class FunctionAction<out T>(
    val check: () -> Boolean,
    val willCancel: Boolean = false,
    initialState: Boolean = check(),
    val execute: () -> T,
) {
    open var isCanceled = false
    private var lastState = initialState

    operator fun invoke(): T? {
        val currentState = check()
        val returnValue =
            if (currentState && !lastState && !isCanceled) {
                isCanceled = willCancel
                execute()
            } else {
                null
            }

        lastState = currentState
        return returnValue
    }

    open fun invertCheck() = FunctionAction<T>({ !check() }, willCancel, execute = execute)
}
