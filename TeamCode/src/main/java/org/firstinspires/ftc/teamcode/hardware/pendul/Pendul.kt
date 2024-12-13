package org.firstinspires.ftc.teamcode.hardware.pendul

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.actionUtil.CancelableAction
import org.firstinspires.ftc.teamcode.hardware.pendul.PendulPosition

class Pendul(val pendulManual: PendulManual) : CancelableAction by pendulManual {
    constructor(hardwareMap: HardwareMap) : this(PendulManual(hardwareMap))

    /**
     * Enum target position of pendul
     *
     * Setting this to [PendulPosition.UNKNOWN] will not change the value
     */
    var targetPosition = PendulPosition.DOWN
        set(value) {
            field = value
            require(value != PendulPosition.UNKNOWN)
            pendulManual.targetPosition = field.positionValue.toDouble()
        }
}