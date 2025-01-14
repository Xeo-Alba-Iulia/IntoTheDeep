package org.firstinspires.ftc.teamcode.pedroPathing

import com.pedropathing.follower.FollowerConstants
import com.pedropathing.localization.Encoder
import com.pedropathing.localization.Localizers
import com.pedropathing.localization.constants.ThreeWheelConstants

@Suppress("unused")
object LConstants {
    init {
        FollowerConstants.localizers = Localizers.THREE_WHEEL
        ThreeWheelConstants.forwardTicksToInches = 0.00052189
        ThreeWheelConstants.strafeTicksToInches = 0.00052189
        ThreeWheelConstants.turnTicksToInches = 0.00053717
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