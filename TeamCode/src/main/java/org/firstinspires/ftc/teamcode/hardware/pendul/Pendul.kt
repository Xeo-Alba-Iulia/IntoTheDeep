package org.firstinspires.ftc.teamcode.hardware.pendul

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.actionUtil.CancelableAction


class Pendul private constructor(private val pendulManual: PendulManual) : CancelableAction by pendulManual {
    constructor(hardwareMap: HardwareMap) : this(PendulManual(hardwareMap))

    var targetPosition = PendulPosition.DOWN
        set(value) {
            field = value
            pendulManual.targetPosition = field.positionValue.toDouble()
        }
}