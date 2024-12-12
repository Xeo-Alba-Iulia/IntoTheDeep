package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.roadrunner.Action

interface ManualPositionMechanism : Action {
    /**
     * Target position of the mechanism
     */
    var targetPosition: Double
}