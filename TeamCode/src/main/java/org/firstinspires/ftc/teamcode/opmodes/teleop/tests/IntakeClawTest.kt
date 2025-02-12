package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.Claw
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp

@TeleOp(name = "Intake claw test", group = "C")
class IntakeClawTest : ManualMechanismTeleOp(::Claw)
