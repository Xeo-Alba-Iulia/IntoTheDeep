package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.roadrunner.Action

interface CancelableAction : Action {
    /**
     * Stops the action as soon as possible
     */
    fun cancel()
}