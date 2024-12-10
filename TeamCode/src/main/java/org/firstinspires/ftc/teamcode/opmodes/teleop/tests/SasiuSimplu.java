package org.firstinspires.ftc.teamcode.opmodes.teleop.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RobotHardware;

/**
 * Cel mai basic TeleOp posibil
 */
@TeleOp(name = "SasiuSimplu", group = "A")
public class SasiuSimplu extends OpMode {
    RobotHardware robot;

    @Override
    public void init() {
        robot = new RobotHardware(hardwareMap);
    }

    @Override
    public void loop() {
        robot.setPower(gamepad1.a ? 1.0 : 0.0);
    }
}
