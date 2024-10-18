package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.RobotHardware;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends OpMode {
    RobotHardware robot;
    int pos = 0;

    @Override
    public void init() {
        robot = new RobotHardware(hardwareMap);
    }

    @Override
    public void loop() {
        pos +=(int) (gamepad2.right_stick_x * 5);

        robot.control(gamepad1);
        robot.sistems(gamepad2, pos);
    }
}