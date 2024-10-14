package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.MotorHardwareInterface
import org.firstinspires.ftc.teamcode.hardware.MovementHardware
import org.firstinspires.ftc.teamcode.hardware.MovementHardwareInterface
import org.firstinspires.ftc.teamcode.hardware.PendulHardware
import org.firstinspires.ftc.teamcode.hardware.PendulHardwareInterface

/**
 * Class containing all hardware of the robot
 * Main class to interact with the robot hardware
 *
 * @property movementHardware The movement hardware of the robot (All motors used in movement)
 * @param hardwareMap The hardware map from the OpMode
 * @constructor Default constructs everything using just hardwareMap,
 * but allows for custom hardware classes to be passed in for testing
 */
class RobotHardware(
    hardwareMap: HardwareMap,
    private val movementHardware: MovementHardwareInterface = MovementHardware(hardwareMap),
    private val pendulHardware: PendulHardwareInterface = PendulHardware(hardwareMap),
):  MotorHardwareInterface,
    MovementHardwareInterface by movementHardware,
    PendulHardwareInterface by pendulHardware
{
    /**
     * Since Java does not support default parameters, this constructor is provided for Java OpModes
     *
     * @param hardwareMap The hardware map from the OpMode
     */
    constructor(hardwareMap: HardwareMap): this(
        hardwareMap,
        MovementHardware(hardwareMap),
        PendulHardware(hardwareMap),
    )

    // Set the mode for both movement and pendul hardware
    override fun setMode(mode: DcMotor.RunMode) {
        movementHardware.setMode(mode)
        pendulHardware.setMode(mode)
    }

    // Set the zero power behavior for both movement and pendul hardware
    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        movementHardware.setZeroPowerBehavior(behavior)
        pendulHardware.setZeroPowerBehavior(behavior)
    }

    // Set the power for both movement and pendul hardware
    override fun setPower(power: Double) {
        movementHardware.setPower(power)
        pendulHardware.setPower(power)
    }
}