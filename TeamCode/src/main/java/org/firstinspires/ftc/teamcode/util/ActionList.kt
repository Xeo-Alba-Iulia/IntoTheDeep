package org.firstinspires.ftc.teamcode.util

class ActionList<in T : FunctionAction<*>>(
    private val actionList: MutableList<T>,
) {
    constructor(vararg actions: T) : this(mutableListOf(*actions))

    operator fun invoke() {
        for (action in actionList) {
            action()
        }
        actionList.removeIf(FunctionAction<*>::isCanceled)
    }

    fun add(element: T) = actionList.add(element)

    fun addAll(elements: Collection<T>) = actionList.addAll(elements)

    fun addAll(elements: Iterable<T>) = actionList.addAll(elements)

    fun addAll(elements: Sequence<T>) = actionList.addAll(elements)

    fun addAll(elements: Array<out T>) = actionList.addAll(elements)
}
