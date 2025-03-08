package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.pedropathing.follower.Follower
import com.pedropathing.follower.FollowerConstants
import com.pedropathing.localization.Pose
import com.pedropathing.util.Constants
import com.pedropathing.util.PIDFController
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.RobotLog
import com.rowanmcalpin.nextftc.core.command.CommandManager
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.HoldPosition
import com.rowanmcalpin.nextftc.pedro.PedroOpMode
import org.firstinspires.ftc.teamcode.nextftc.DriverControlled
import org.firstinspires.ftc.teamcode.nextftc.Intake
import org.firstinspires.ftc.teamcode.nextftc.Outtake
import org.firstinspires.ftc.teamcode.nextftc.subsystems.Lift
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeClaw
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeClawRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.Claw
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.util.autoPose
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants
import kotlin.math.abs

@TeleOp
open class NextTeleOp :
    PedroOpMode(
        Intake,
        Outtake,
    ) {
    val inTransfer
        get() =
            listOf(
                !Intake.isExtended,
                Outtake.targetPosition == OuttakePosition.TRANSFER,
                Lift.inTransfer,
            ).all { it }

    private var holdHeading = false
    private val headingPIDFCoefficients = FollowerConstants.headingPIDFCoefficients
    private val headingPIDF = PIDFController(headingPIDFCoefficients)

    init {
        headingPIDF.targetPosition = 0.0
    }

    private val headingPIDValue: Double
        get() {
            follower.headingOffset +=
                gamepadManager.gamepad1.rightStick.x
                    .toDouble()
            val modifiedHeading =
                if (follower.pose.heading <= Math.PI) {
                    follower.pose.heading
                } else {
                    -2 * Math.PI + follower.pose.heading
                }

            RobotLog.d("Modified PID heading: $modifiedHeading")

            headingPIDF.updatePosition(modifiedHeading)
            return headingPIDF.runPIDF()
        }

    override fun onInit() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        follower = Follower(hardwareMap)
        follower.setStartingPose(autoPose)
    }

    val stickEnableProfileCurve: (Float) -> Float = {
        if (abs(it) > 0.6f) it else 0.0f
    }

    override fun onStartButtonPressed() {
        DriverControlled(
            gamepadManager.gamepad1.leftStick.y::toDouble,
            gamepadManager.gamepad1.leftStick.x::toDouble,
            {
                if (holdHeading) {
                    headingPIDValue
                } else {
                    gamepadManager.gamepad1.rightStick.x
                        .toDouble()
                }
            },
            robotCentric = false,
        )()

        gamepadManager.gamepad1.rightBumper.pressedCommand = {
            if (Intake.isExtended) Intake.pickup else NullCommand()
        }
        gamepadManager.gamepad1.leftBumper.pressedCommand = {
            if (Intake.isExtended) IntakeClaw.open else NullCommand()
        }
        gamepadManager.gamepad1.leftTrigger.pressedCommand = {
            if (Intake.isExtended) {
                SequentialGroup(IntakeClaw.open, IntakeClawRotate.goLeft)
            } else {
                NullCommand()
            }
        }
        gamepadManager.gamepad1.rightTrigger.pressedCommand = {
            if (Intake.isExtended) {
                SequentialGroup(IntakeClaw.open, IntakeClawRotate.goRight)
            } else {
                NullCommand()
            }
        }
        gamepadManager.gamepad1.cross.pressedCommand = { InstantCommand { holdHeading = !holdHeading } }

        gamepadManager.gamepad1.leftStick.button.pressedCommand = {
            InstantCommand { follower.pose = Pose() }
        }

        gamepadManager.gamepad2.rightStick.profileCurve = stickEnableProfileCurve
        gamepadManager.gamepad2.rightStick.displacedCommand = {
            if (it.second < 0) {
                Lift.resetLift
            } else {
                NullCommand()
            }
        }
        gamepadManager.gamepad2.leftStick.displacedCommand = {
            if (it.second > 0) {
                ParallelGroup(Pendul.goTransfer, Lift.toHang)
            } else {
                NullCommand()
            }
        }

        gamepadManager.gamepad2.rightBumper.releasedCommand = {
            if (!Claw.isClosed) {
                holdHeading = false
            }
            if (Claw.isClosed && Outtake.targetPosition == OuttakePosition.BAR) {
                // TODO: Muta pendulul ca sa iasa mai usor
            }
            Claw.toggle
        }

        gamepadManager.gamepad2.dpadLeft.pressedCommand = {
            SequentialGroup(Lift.toHang, Outtake.goBar)
        }

        gamepadManager.gamepad2.circle.pressedCommand = {
            SequentialGroup(Claw.close, IntakeClaw.open, Lift.toHighBar, Outtake.goBar)
        }

        gamepadManager.gamepad2.dpadDown.pressedCommand = {
            SequentialGroup(Lift.toLow, Outtake.goPickup)
        }

        gamepadManager.gamepad2.rightStick.button.pressedCommand = {
            ParallelGroup(Lift.toHighBar, Outtake.goTransfer)
        }

        gamepadManager.gamepad2.leftStick.button.pressedCommand = {
            if (CommandManager.findConflicts(Lift.toHang).firstOrNull() is HoldPosition) {
                Lift.toHang
            } else {
                NullCommand()
            }
        }
        gamepadManager.gamepad2.dpadRight.pressedCommand = {
            ParallelGroup(
                Outtake.goTransfer.afterTime(1),
                SequentialGroup(
                    Intake.toTransfer,
                    Lift.toTransfer,
                    Outtake.goTransfer,
                ),
            )
        }
    }
}
