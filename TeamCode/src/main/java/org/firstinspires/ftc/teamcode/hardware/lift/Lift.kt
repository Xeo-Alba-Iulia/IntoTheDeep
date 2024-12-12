package org.firstinspires.ftc.teamcode.hardware.lift

import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class Lift private constructor(private val liftManual: LiftManual) : Action by liftManual {
    constructor(hardwareMap: HardwareMap) : this(LiftManual(hardwareMap, isVerbose = false))

    var targetPosition = Position.DOWN
        set(value) {
            field = value
            liftManual.targetPosition = field.positionValue.toDouble()
        }
}