package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.MotorHardwareInterface
import org.firstinspires.ftc.teamcode.hardware.MovementHardware
import org.firstinspires.ftc.teamcode.hardware.MovementHardwareInterface
import org.firstinspires.ftc.teamcode.hardware.PendulHardware
import org.firstinspires.ftc.teamcode.hardware.PendulHardwareInterface
import org.firstinspires.ftc.teamcode.hardware.LiftHardware
import org.firstinspires.ftc.teamcode.hardware.LiftHardwareInterface

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
    private val liftHardware: LiftHardwareInterface = LiftHardware(hardwareMap)
):  MotorHardwareInterface,
    MovementHardwareInterface by movementHardware,
    PendulHardwareInterface by pendulHardware,
    LiftHardwareInterface by liftHardware
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
        LiftHardware(hardwareMap)
    )

    override fun setPower(power: Double) =
        movementHardware.setPower(power)

    override fun setMode(mode: DcMotor.RunMode) =
        movementHardware.setMode(mode)

    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) =
        movementHardware.setZeroPowerBehavior(behavior)

    fun control(gamepad: Gamepad) {
        move(gamepad)
    }

    fun sistems(gamepad: Gamepad) {
        lift(gamepad)
    }

    @Deprecated("Use move instead", ReplaceWith("move(gamepad)"), DeprecationLevel.ERROR)
    fun movement(gamepad: Gamepad) = move(gamepad)
}