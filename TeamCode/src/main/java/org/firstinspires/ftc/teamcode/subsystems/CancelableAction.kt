package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.roadrunner.Action

interface CancelableAction : Action {
    fun cancel()
}