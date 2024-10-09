package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.RobotHardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Kickathon")
public class TeleOp extends OpMode {
    RobotHardware robot;

    @Override
    public void init() {
        robot = new RobotHardware(hardwareMap);
    }

    public void lift() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void loop() {
        robot.move(gamepad1);
//        lift();
    }
}