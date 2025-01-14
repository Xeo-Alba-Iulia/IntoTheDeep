package pedroPathing.constants

import com.pedropathing.follower.FollowerConstants
import com.pedropathing.localization.Localizers
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Suppress("unused")
object FConstants {
    init {
        FollowerConstants.localizers = Localizers.THREE_WHEEL

        FollowerConstants.leftFrontMotorName = "MotorFrontLeft"
        FollowerConstants.leftRearMotorName = "MotorBackLeft"
        FollowerConstants.rightFrontMotorName = "MotorFrontRight"
        FollowerConstants.rightRearMotorName = "MotorBackRight"

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD

        FollowerConstants.mass = TODO("Set robot mass")

        FollowerConstants.xMovement = 81.34056
        FollowerConstants.yMovement = 65.43028

        FollowerConstants.forwardZeroPowerAcceleration = -41.278
        FollowerConstants.lateralZeroPowerAcceleration = -59.7819

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.1, 0.0, 0.0, 0.0)
        FollowerConstants.useSecondaryTranslationalPID = false

        FollowerConstants.headingPIDFCoefficients.setCoefficients(2.0, 0.0, 0.1, 0.0)
        FollowerConstants.useSecondaryHeadingPID = false

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.1, 0.0, 0.0, 0.6, 0.0)
        FollowerConstants.useSecondaryDrivePID = false

        FollowerConstants.zeroPowerAccelerationMultiplier = 4.0
        FollowerConstants.centripetalScaling = 0.0005

        FollowerConstants.pathEndTimeoutConstraint = 500.0
        FollowerConstants.pathEndTValueConstraint = 0.995
        FollowerConstants.pathEndVelocityConstraint = 0.1
        FollowerConstants.pathEndTranslationalConstraint = 0.1
        FollowerConstants.pathEndHeadingConstraint = 0.007
    }
}