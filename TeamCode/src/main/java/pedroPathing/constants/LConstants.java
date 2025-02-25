package pedroPathing.constants;

import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.ThreeWheelConstants;
import com.pedropathing.localization.constants.ThreeWheelIMUConstants;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.0029961808552337846;
        ThreeWheelConstants.strafeTicksToInches = 0.002999044221628336;
        ThreeWheelConstants.turnTicksToInches = 0.002928990471730247;
        ThreeWheelConstants.leftY = 5.314961;
        ThreeWheelConstants.rightY = -5.314961;
        ThreeWheelConstants.strafeX = 3.74016;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "MotorBackLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "MotorFrontRight";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "MotorFrontLeft";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;


        ThreeWheelIMUConstants.forwardTicksToInches = 0.0029961808552337846;
        ThreeWheelIMUConstants.strafeTicksToInches = 0.002999044221628336;
        ThreeWheelIMUConstants.turnTicksToInches = 0.002928990471730247;
        ThreeWheelIMUConstants.leftY = 5.314961;
        ThreeWheelIMUConstants.rightY = -5.314961;
        ThreeWheelIMUConstants.strafeX = 3.74016;
        ThreeWheelIMUConstants.leftEncoder_HardwareMapName = "MotorBackLeft";
        ThreeWheelIMUConstants.rightEncoder_HardwareMapName = "MotorFrontRight";
        ThreeWheelIMUConstants.strafeEncoder_HardwareMapName = "MotorFrontLeft";
        ThreeWheelIMUConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelIMUConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelIMUConstants.strafeEncoderDirection = Encoder.FORWARD;

        ThreeWheelIMUConstants.IMU_HardwareMapName = "imu";
        ThreeWheelIMUConstants.IMU_Orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.DOWN
        );
    }
}
