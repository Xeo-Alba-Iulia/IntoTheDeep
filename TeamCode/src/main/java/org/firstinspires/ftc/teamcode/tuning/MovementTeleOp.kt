package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.Movement

@TeleOp(name = "Movement Tele OP")
class MovementTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val movement = Movement(this.hardwareMap)

        waitForStart()

        while (opModeIsActive()) {
            movement.move(this.gamepad1)
        }
    }
}