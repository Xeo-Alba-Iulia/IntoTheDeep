package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakeClawRotate
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp

@TeleOp
class IntakeClawRotateTest : ManualMechanismTeleOp(::IntakeClawRotate)
