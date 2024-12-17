package org.firstinspires.ftc.teamcode.subsystems

interface ManualPositionMechanism : CancelableAction {
    /**
     * Target position of the mechanism
     */
    var targetPosition: Double
}