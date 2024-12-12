package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.roadrunner.Action

interface ManualPositionMechanism : Action {
    var targetPosition: Double
}