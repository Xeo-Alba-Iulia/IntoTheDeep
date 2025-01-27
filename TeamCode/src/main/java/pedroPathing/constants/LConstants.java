package pedroPathing.constants;

import com.pedropathing.localization.Encoder;
import com.pedropathing.localization.constants.ThreeWheelConstants;

public class LConstants {
    static {
        ThreeWheelConstants.forwardTicksToInches = 0.002959498594166741;
        ThreeWheelConstants.strafeTicksToInches = -0.00314368383040309;
        ThreeWheelConstants.turnTicksToInches = 0.0029836080519608822;
        ThreeWheelConstants.leftY = 5.314961;
        ThreeWheelConstants.rightY = -5.314961;
        ThreeWheelConstants.strafeX = -3.937017;
        ThreeWheelConstants.leftEncoder_HardwareMapName = "LiftLeft";
        ThreeWheelConstants.rightEncoder_HardwareMapName = "MotorBackLeft";
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "MotorBackRight";
        ThreeWheelConstants.leftEncoderDirection = Encoder.FORWARD;
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE;
        ThreeWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
    }
}