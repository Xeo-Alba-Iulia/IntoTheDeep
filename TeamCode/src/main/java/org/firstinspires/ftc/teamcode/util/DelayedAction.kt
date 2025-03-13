package org.firstinspires.ftc.teamcode.util

import kotlin.time.ComparableTimeMark
import kotlin.time.Duration
import kotlin.time.TimeSource

class DelayedAction<out T>(
    val endTimeMark: ComparableTimeMark,
    execute: () -> T,
) : FunctionAction<T>(check = endTimeMark::hasPassedNow, willCancel = true, execute = execute),
    Comparable<DelayedAction<*>> {
    constructor(delay: Duration, execute: () -> T) : this(
        TimeSource.Monotonic.markNow() + delay,
        execute,
    )

    override fun compareTo(other: DelayedAction<*>) = this.endTimeMark.compareTo(other.endTimeMark)

    // Inverting the check on delayedActions just leads to instant execution, so we throw an exception
    override fun invert() =
        throw UnsupportedOperationException(
            "invert() is not supported for DelayedAction.",
        )
}
