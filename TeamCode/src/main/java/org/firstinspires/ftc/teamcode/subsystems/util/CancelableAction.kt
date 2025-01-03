package org.firstinspires.ftc.teamcode.subsystems.util

import com.acmerobotics.roadrunner.Action

interface CancelableAction : Action {
    fun cancel()
}