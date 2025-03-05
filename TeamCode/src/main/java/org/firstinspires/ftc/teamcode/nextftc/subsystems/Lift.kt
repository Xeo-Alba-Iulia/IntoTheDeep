package org.firstinspires.ftc.teamcode.nextftc.subsystems

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.*
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.LambdaCommand
import com.rowanmcalpin.nextftc.ftc.OpModeData.hardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.abs

private class ResetLiftCommand(
    private val liftMotor: DcMotorEx,
) : Command() {
    override val interruptible = false

//    override val subsystems = setOf(Lift)
    override var isDone = false

    private var ticksRan = 0
    private var current = 0.0

    override fun start() {
        liftMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        liftMotor.power = -0.25
    }

    override fun update() {
        if (isDone) {
            return
        }
        if (ticksRan > 40) {
            if (abs(current - liftMotor.getCurrent(CurrentUnit.MILLIAMPS)) > 150) {
                stop(false)
                isDone = true
            }
        } else {
            current = liftMotor.getCurrent(CurrentUnit.MILLIAMPS)
        }

        ticksRan++
    }

    override fun stop(interrupted: Boolean) {
        liftMotor.targetPosition = 0
        liftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        liftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }
}

private class GoToPositionCommand(
    private val position: Int,
) : Command() {
    override val isDone
        get() = abs(Lift.measuredPosition - targetPosition) < 20

    override val subsystems = setOf(Lift)

    override fun start() {
        targetPosition = position
    }
}

private var targetPosition = 0

private class LiftPositions private constructor() {
    companion object {
        @JvmField @Volatile
        var down = 0

        @JvmField @Volatile
        var transfer = 100

        @JvmField @Volatile
        var highBar = 650

        @JvmField @Volatile
        var hang = 1700

        @JvmField @Volatile
        var highBasket = 3200
    }
}

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] instance from OpMode
 */
@Config
object Lift : Subsystem() {
    val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    private val liftMotors = listOf(liftLeft to "LiftLeft", liftRight to "LiftRight")

    var measuredPosition = 0

    init {
        for ((motor, _) in liftMotors) {
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun periodic() {
        measuredPosition = liftMotors.map { it.first.currentPosition }.average().toInt()
        for ((motor, name) in liftMotors) {
            FtcDashboard.getInstance().telemetry.addData("Position for $name", motor.currentPosition)
        }
        FtcDashboard.getInstance().telemetry.addData("Lift target position", targetPosition)
    }

    private var resetCommands = mutableListOf<ResetLiftCommand>()

    val resetLift
        get() =
            LambdaCommand()
                .setStart {
                    resetCommands = mutableListOf(ResetLiftCommand(liftLeft), ResetLiftCommand(liftRight))
                    resetCommands.forEach { it.start() }
                }.setUpdate {
                    resetCommands.forEach { it.update() }
                }.setInterruptible(false)
                .setIsDone { resetCommands.all { it.isDone } }
                .setStop {
                    resetCommands.forEach { command -> command.stop(it) }
                    resetCommands.clear()
                    targetPosition = 0
                }.setSubsystem(Lift)

    val goDown: Command
        get() = GoToPositionCommand(LiftPositions.down)

    val goTransfer: Command
        get() = GoToPositionCommand(LiftPositions.transfer)

    val goHighBar: Command
        get() = GoToPositionCommand(LiftPositions.highBar)

    val goHang: Command
        get() = GoToPositionCommand(LiftPositions.hang)

    val goHighBasket: Command
        get() = GoToPositionCommand(LiftPositions.highBasket)

    val inTransfer
        get() = targetPosition == LiftPositions.transfer
}
