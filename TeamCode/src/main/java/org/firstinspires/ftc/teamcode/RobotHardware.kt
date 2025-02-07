package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.util.Constants
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.Outtake
import org.firstinspires.ftc.teamcode.systems.subsystems.Lift
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.Extend
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakeMotor
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakePendul
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Claw
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

/**
 * Class containing all hardware of the robot
 * Main class to interact with the robot hardware
 *
 * @param hardwareMap The hardware map from the OpMode
 * @constructor Default constructs everything using just hardwareMap,
 * but allows for custom hardware classes to be passed in for testing
 */
class RobotHardware
    @JvmOverloads
    constructor(
        hardwareMap: HardwareMap,
        val lift: Lift = Lift(hardwareMap),
        val outtake: Outtake = Outtake(hardwareMap),
        val intakePendul: IntakePendul = IntakePendul(hardwareMap),
        val intake: IntakeMotor = IntakeMotor(hardwareMap),
        val extend: Extend = Extend(hardwareMap),
        val claw: Claw = Claw(hardwareMap),
    ) : Action {
        init {
            Constants.setConstants(FConstants::class.java, LConstants::class.java)
        }

        override fun run(p: TelemetryPacket) =
            lift.run(p) &&
                outtake.run(p) &&
                intakePendul.run(p) &&
                intake.run(p) &&
                claw.run(p) &&
                extend.run(p)
    }
