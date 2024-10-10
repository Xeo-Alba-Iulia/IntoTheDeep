package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.RobotHardware;

import java.util.Arrays;

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
//        Arrays.stream(robot.getMotors()).forEach(motor -> motor.setPower(gamepad1.a ? 1.0 : 0.0));
    }
}
