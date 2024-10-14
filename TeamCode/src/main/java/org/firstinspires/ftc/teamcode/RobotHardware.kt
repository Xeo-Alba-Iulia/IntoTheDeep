package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
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
 */
class RobotHardware(
    hardwareMap: HardwareMap
) {
    private val movementHardware: MovementHardwareInterface = MovementHardware(hardwareMap)
    private val pendulHardware: PendulHardwareInterface = PendulHardware(hardwareMap)

    // Function to move the robot using the gamepad
    fun move(gamepad: Gamepad) {
        movementHardware.move(gamepad)
    }

    // Set the power for both movement and pendul hardware
    fun setPower(power: Double) {
        movementHardware.setPower(power)
        pendulHardware.setPower(power)
    }

    // Set the mode for both movement and pendul hardware
    fun setMode(mode: DcMotor.RunMode) {
        movementHardware.setMode(mode)
        pendulHardware.setMode(mode)
    }

    // Set the zero power behavior for both movement and pendul hardware
    fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        movementHardware.setZeroPowerBehavior(behavior)
        pendulHardware.setZeroPowerBehavior(behavior)
    }

    fun pendul(gamepad: Gamepad) {
        pendulHardware.pendul(gamepad)
    }

    // Add any additional methods to control specific robot hardware components here
}