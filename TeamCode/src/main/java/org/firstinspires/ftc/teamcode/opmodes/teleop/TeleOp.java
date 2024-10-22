package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.RobotHardware;
import org.firstinspires.ftc.teamcode.hardware.PendulPosition;

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
        robot.sistems(gamepad2);

        PendulPosition pos = null;
        if (gamepad2.a) {
            pos = PendulPosition.DOWN;
        } else if (gamepad2.b) {
            pos = PendulPosition.HALF;
        } else if (gamepad2.y) {
            pos = PendulPosition.UP;
        }

        if (pos == null) {
            return;
        }
        robot.setPendul(pos);

        telemetry.addData("Pendul position:", robot.getPendulHardware().getCurrentPosition());
        telemetry.update();
    }
}