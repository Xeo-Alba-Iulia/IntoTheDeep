package pedroPathing.constants;

import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.ThreeWheelConstants;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.002959498594166741;
        ThreeWheelConstants.strafeTicksToInches = -0.00314368383040309;
        ThreeWheelConstants.turnTicksToInches = 0.0029836080519608822;
        ThreeWheelConstants.leftY = 3.70079;
        ThreeWheelConstants.rightY = -3.70079;
        ThreeWheelConstants.strafeX = -5.67;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "MotorBackLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "LiftLeft";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "MotorBackRight";
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.REVERSE;
    }
}