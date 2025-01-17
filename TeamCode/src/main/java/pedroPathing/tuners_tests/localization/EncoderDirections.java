package pedroPathing.tuners_tests.localization;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.ThreeWheelConstants;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

import java.util.List;

@TeleOp(name = "Encoder Directions", group = "Teleop Test")
public class EncoderDirections extends OpMode {
    private Telemetry telemetryA;

    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private Encoder strafeEncoder;

    private double leftEncoderPosition;
    private double rightEncoderPosition;
    private double strafeEncoderPosition;

    private List<Encoder> encoders;

    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        this.leftEncoder = new Encoder(this.hardwareMap.get(DcMotorEx.class, ThreeWheelConstants.leftEncoder_HardwareMapName));
        this.rightEncoder = new Encoder(this.hardwareMap.get(DcMotorEx.class, ThreeWheelConstants.rightEncoder_HardwareMapName));
        this.strafeEncoder = new Encoder(this.hardwareMap.get(DcMotorEx.class, ThreeWheelConstants.strafeEncoder_HardwareMapName));

        this.leftEncoder.setDirection(ThreeWheelConstants.leftEncoderDirection);
        this.rightEncoder.setDirection(ThreeWheelConstants.rightEncoderDirection);
        this.strafeEncoder.setDirection(ThreeWheelConstants.strafeEncoderDirection);

        encoders = List.of(leftEncoder, rightEncoder, strafeEncoder);

        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryA.addData("This will allow you to test the directions of your encoders. You can change the directions in FTCDashboard -> LocalizerConstants.", "");
        telemetryA.addData("Restart OpMode to view the changes you made.", "");
        telemetryA.update();
    }

    @Override
    public void loop() {
        Constants.setConstants(FConstants.class, LConstants.class);

        for (Encoder encoder : encoders) {
            encoder.update();
        }

        this.leftEncoderPosition += this.leftEncoder.getDeltaPosition();
        this.rightEncoderPosition += this.rightEncoder.getDeltaPosition();
        this.strafeEncoderPosition += this.strafeEncoder.getDeltaPosition();

        telemetryA.addData("Left Encoder Ticks", this.leftEncoderPosition);
        telemetryA.addData("Right Encoder Ticks", this.rightEncoderPosition);
        telemetryA.addData("Strafe Encoder Ticks", this.strafeEncoderPosition);
    }
}
