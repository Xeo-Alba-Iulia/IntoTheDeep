package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.MovementHardware
import org.firstinspires.ftc.teamcode.hardware.MovementHardwareInterface

/**
 * Class containing all hardware of the robot
 * Main class to interact with the robot hardware
 *
 * @property movementHardware The movement hardware of the robot (All motors used in movement)
 * @param hardwareMap The hardware map from the OpMode
 */
class RobotHardware(
    hardwareMap: HardwareMap,
    private val movementHardware: MovementHardwareInterface = MovementHardware(hardwareMap)
) : MovementHardwareInterface by movementHardware {
    /**
     * Default hardware constructor
     */
    constructor(hardwareMap: HardwareMap): this(hardwareMap, MovementHardware(hardwareMap))

    @Deprecated("Use move instead", ReplaceWith("move(gamepad)"))
    fun movement(gamepad: Gamepad) = move(gamepad)
}