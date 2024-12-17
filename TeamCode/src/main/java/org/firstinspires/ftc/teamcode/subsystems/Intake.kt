package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Intake subsystem
 *
 * @param hardwareMap the [HardwareMap] object from the OpMode
 */
class Intake(hardwareMap: HardwareMap) : Action {
    private val intakeMotor = hardwareMap.crservo.get("IntakeMotor")

    init {
        intakeMotor.direction = DcMotorSimple.Direction.REVERSE
    }

    // Implementing Action interface for the actual Intake

    /**
     * Power to set the intake to
     */
    var intakePower: Double = 0.6
        set(value) {
            require(value in -1.0..1.0) { "Intake power must be between 0 and 1" }
            field = value
        }

    override fun run(p: TelemetryPacket): Boolean {
        intakeMotor.power = intakePower
        p.put("Intake Motor Power", intakePower)
        return true
    }
}