package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.teamcode.hardware.actionUtil.CancelableAction

interface ManualPositionMechanism : CancelableAction {
    /**
     * Target position of the mechanism
     */
    var targetPosition: Double
}