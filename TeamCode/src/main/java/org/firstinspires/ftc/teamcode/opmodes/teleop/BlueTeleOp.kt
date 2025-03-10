package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.AllianceColor

@TeleOp
class BlueTeleOp : MainTeleOp() {
    override val allianceColor = AllianceColor.BLUE
}
