package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.Intake
import org.firstinspires.ftc.teamcode.hardware.Lift
import org.firstinspires.ftc.teamcode.hardware.MotorHardwareInterface
import org.firstinspires.ftc.teamcode.hardware.Movement
import org.firstinspires.ftc.teamcode.hardware.MovementHardwareInterface

/**
 * Class containing all hardware of the robot
 * Main class to interact with the robot hardware
 *
 * @property movementHardware The movement hardware of the robot (All motors used in movement)
 * @param hardwareMap The hardware map from the OpMode
 * @constructor Default constructs everything using just hardwareMap,
 * but allows for custom hardware classes to be passed in for testing
 */
class RobotHardware @JvmOverloads constructor(
    hardwareMap: HardwareMap,
    val movementHardware: Movement = Movement(hardwareMap),
    val lift: Lift = Lift(hardwareMap),
    val pendulHardware: RobotHardware = RobotHardware(hardwareMap),
    val intake: Intake = Intake(hardwareMap)
):  MotorHardwareInterface, MovementHardwareInterface by movementHardware
{
    override fun setPower(power: Double) =
        movementHardware.setPower(power)

    override fun setMode(mode: DcMotor.RunMode) =
        movementHardware.setMode(mode)

    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) =
        movementHardware.setZeroPowerBehavior(behavior)



    @Deprecated("Use move instead", ReplaceWith("move(gamepad)"), DeprecationLevel.ERROR)
    fun movement(gamepad: Gamepad) = move(gamepad)
}