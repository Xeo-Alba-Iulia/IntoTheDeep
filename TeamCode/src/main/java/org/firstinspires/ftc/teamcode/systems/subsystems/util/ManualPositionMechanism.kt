package org.firstinspires.ftc.teamcode.systems.subsystems.util

/**
 * Mechanism that can be set to a target position and be canceled
 */
interface ManualPositionMechanism : CancelableAction {
    /**
     * Target position of the mechanism
     *
     * @throws IllegalArgumentException if the target position is invalid`
     */
    var targetPosition: Double
}
