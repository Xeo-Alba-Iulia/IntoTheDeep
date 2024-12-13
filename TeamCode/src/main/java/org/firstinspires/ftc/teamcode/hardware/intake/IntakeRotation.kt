package org.firstinspires.ftc.teamcode.hardware.intake

import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeRotation
private constructor(private val intakeRotationManual: IntakeRotationManual) : Action by intakeRotationManual {
    constructor(hardwareMap: HardwareMap) : this(IntakeRotationManual(hardwareMap))

    var targetPosition: IntakeRotationPosition = IntakeRotationPosition.PARALLEL
        set(value) {
            field = value
            intakeRotationManual.targetPosition = field.positionValue
        }
}