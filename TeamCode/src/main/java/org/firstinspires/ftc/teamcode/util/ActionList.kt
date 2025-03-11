package org.firstinspires.ftc.teamcode.util

class ActionList<in T : FunctionAction<*>>(
    private val actionList: MutableList<T>,
) {
    constructor(vararg actions: T) : this(mutableListOf(*actions))

    private val commandsToAdd = mutableListOf<T>()

    operator fun invoke() {
        for (action in actionList) {
            action()
        }
        actionList.removeIf(FunctionAction<*>::isCanceled)
        actionList += commandsToAdd
    }

    fun add(element: T) = commandsToAdd.add(element)

    fun addAll(elements: Collection<T>) = commandsToAdd.addAll(elements)

    fun addAll(elements: Iterable<T>) = commandsToAdd.addAll(elements)

    fun addAll(elements: Sequence<T>) = commandsToAdd.addAll(elements)

    fun addAll(elements: Array<out T>) = commandsToAdd.addAll(elements)

    operator fun plusAssign(element: T) {
        commandsToAdd += element
    }

    operator fun plusAssign(elements: Collection<T>) {
        commandsToAdd += elements
    }

    operator fun plusAssign(elements: Iterable<T>) {
        commandsToAdd += elements
    }

    operator fun plusAssign(elements: Sequence<T>) {
        commandsToAdd += elements
    }

    operator fun plusAssign(elements: Array<out T>) {
        commandsToAdd += elements
    }
}
