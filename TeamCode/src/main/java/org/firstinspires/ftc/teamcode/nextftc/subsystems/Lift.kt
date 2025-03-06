package org.firstinspires.ftc.teamcode.nextftc.subsystems

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.utility.LambdaCommand
import com.rowanmcalpin.nextftc.core.control.controllers.Controller
import com.rowanmcalpin.nextftc.core.control.controllers.SqrtController
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward
import com.rowanmcalpin.nextftc.ftc.OpModeData.hardwareMap
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.*
import kotlin.math.abs

private class ResetLiftCommand(
    private val liftMotor: Controllable,
) : Command() {
    override val interruptible = false
    override val subsystems = setOf(Lift)
    private var ticksRan = 0

    override val isDone
        get() = ticksRan >= 40 && liftMotor.velocity < 5.0

    override fun start() {
        liftMotor.power = -0.25
    }

    override fun update() {
        ticksRan++
    }

    override fun stop(interrupted: Boolean) {
        if (interrupted) {
            throw IllegalStateException("Lift reset was interrupted")
        }
        liftMotor.currentPosition = 0.0
    }
}

private class LiftPositions private constructor() {
    companion object {
        @JvmField @Volatile
        var down = 0.0

        @JvmField @Volatile
        var transfer = 100.0

        @JvmField @Volatile
        var highBar = 650.0

        @JvmField @Volatile
        var hang = 1700.0

        @JvmField @Volatile
        var highBasket = 3200.0
    }
}

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] instance from OpMode
 */
@Config
object Lift : Subsystem() {
    private lateinit var liftLeft: MotorEx
    private lateinit var liftRight: MotorEx

    private lateinit var motorGroup: MotorGroup
    private lateinit var controller: Controller

    override fun initialize() {
        liftLeft = MotorEx("LiftLeft")
        liftRight = MotorEx("LiftRight")

        motorGroup = MotorGroup(liftLeft, liftRight)
        controller = SqrtController(kP = 10.0, kF = StaticFeedforward(1.0), setPointTolerance = 20.0)
    }

    var isHoldingPosition = true
        private set

    override val defaultCommand
        get() =
            ParallelGroup(
                HoldPosition(motorGroup, controller, this),
                LambdaCommand()
                    .setIsDone { false }
                    .setStart { isHoldingPosition = true }
                    .setStop { isHoldingPosition = false },
            )

    val resetLiftCommand get() = ResetLiftCommand(motorGroup) as Command

    val inTransfer
        get() = isHoldingPosition && abs(motorGroup.currentPosition - LiftPositions.transfer) <= 20.0

    val toLow get() = RunToPosition(motorGroup, LiftPositions.down, controller, this)
    val toHighBar get() = RunToPosition(motorGroup, LiftPositions.highBar, controller, this)
    val toHighBasket get() = RunToPosition(motorGroup, LiftPositions.highBasket, controller, this)
    val toTransfer get() = RunToPosition(motorGroup, LiftPositions.transfer, controller, this)

    val toHang get() = RunToPosition(motorGroup, LiftPositions.hang, controller, this)
}
