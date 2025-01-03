package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.subsystems.Extend
import org.firstinspires.ftc.teamcode.subsystems.Lift
import org.firstinspires.ftc.teamcode.subsystems.Movement
import org.firstinspires.ftc.teamcode.subsystems.Pendul

/**
 * Class containing all hardware of the robot
 * Main class to interact with the robot hardware
 *
 * @property movement The movement hardware of the robot (All motors used in movement)
 * @param hardwareMap The hardware map from the OpMode
 * @constructor Default constructs everything using just hardwareMap,
 * but allows for custom hardware classes to be passed in for testing
 */
class RobotHardware @JvmOverloads constructor(
    hardwareMap: HardwareMap,
    val movement: Movement = Movement(hardwareMap),
    val lift: Lift = Lift(hardwareMap),
    val pendul: Pendul = Pendul(hardwareMap),
    val intake: Intake = Intake(hardwareMap),
    val extend: Extend = Extend(hardwareMap)
)