package pedroPathing.constants;

import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.ThreeWheelConstants;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.0029961808552337846;
        ThreeWheelConstants.strafeTicksToInches = 0.002999044221628336;
        ThreeWheelConstants.turnTicksToInches = 0.002938990471730247;
        ThreeWheelConstants.leftY = 5.314961;
        ThreeWheelConstants.rightY = -5.314961;
        ThreeWheelConstants.strafeX = 3.74016;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "MotorBackLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "MotorFrontRight";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "MotorFrontLeft";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}
