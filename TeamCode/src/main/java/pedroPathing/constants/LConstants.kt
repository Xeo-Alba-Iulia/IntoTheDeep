package pedroPathing.constants

import com.pedropathing.follower.FollowerConstants
import com.pedropathing.localization.Encoder
import com.pedropathing.localization.Localizers
import com.pedropathing.localization.constants.ThreeWheelConstants

@Suppress("unused")
object LConstants {
    init {
        FollowerConstants.localizers = Localizers.THREE_WHEEL
        ThreeWheelConstants.forwardTicksToInches = 0.0030287019500411694
        ThreeWheelConstants.strafeTicksToInches = -0.002921072494573048
        ThreeWheelConstants.turnTicksToInches = 0.0030110390874661243
        ThreeWheelConstants.leftY = 3.70079
        ThreeWheelConstants.rightY = -3.70079
        ThreeWheelConstants.strafeX = -5.67
        ThreeWheelConstants.leftEncoder_HardwareMapName = "MotorBackLeft"
        ThreeWheelConstants.rightEncoder_HardwareMapName = "LiftLeft"
        ThreeWheelConstants.strafeEncoder_HardwareMapName = "MotorBackRight"
        ThreeWheelConstants.leftEncoderDirection = Encoder.REVERSE
        ThreeWheelConstants.rightEncoderDirection = Encoder.REVERSE
        ThreeWheelConstants.strafeEncoderDirection = Encoder.REVERSE
    }
}