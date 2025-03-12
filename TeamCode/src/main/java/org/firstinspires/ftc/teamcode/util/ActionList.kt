package org.firstinspires.ftc.teamcode.util

import android.util.Log

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
        commandsToAdd.clear()
    }

    fun add(element: T) = commandsToAdd.add(element)

    fun addAll(elements: Collection<T>) = commandsToAdd.addAll(elements)

    fun addAll(elements: Iterable<T>) = commandsToAdd.addAll(elements)

    fun addAll(elements: Sequence<T>) = commandsToAdd.addAll(elements)

    fun addAll(elements: Array<out T>) = commandsToAdd.addAll(elements)

    operator fun plusAssign(element: T) {
        commandsToAdd += element
        Log.d("ActionList", "Current size: $size")
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

    val size get() = actionList.size + commandsToAdd.size

    fun isEmpty() = actionList.isEmpty() && commandsToAdd.isEmpty()

    fun isNotEmpty() = actionList.isNotEmpty() || commandsToAdd.isNotEmpty()
}
