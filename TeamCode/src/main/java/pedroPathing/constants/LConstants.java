package pedroPathing.constants;

import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.ThreeWheelConstants;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.002959498594166741;
        ThreeWheelConstants.strafeTicksToInches = 0.00314368383040309;
        ThreeWheelConstants.turnTicksToInches = 0.00291696596614687;
        ThreeWheelConstants.leftY = 5.314961;
        ThreeWheelConstants.rightY = -5.314961;
        ThreeWheelConstants.strafeX = 4.01574803;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "MotorBackLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "MotorFrontRight";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "MotorFrontLeft";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}
