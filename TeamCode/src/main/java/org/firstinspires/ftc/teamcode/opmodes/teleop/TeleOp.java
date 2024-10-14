package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.RobotHardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends OpMode {
    RobotHardware robot;

    @Override
    public void init() {
        robot = new RobotHardware(hardwareMap);
    }

    @Override
    public void loop() {
        robot.control(gamepad1);
    }
}