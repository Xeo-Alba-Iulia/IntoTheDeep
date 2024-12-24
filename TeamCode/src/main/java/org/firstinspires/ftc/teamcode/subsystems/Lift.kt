package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.constantProfile
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.PIDCoefficients
import org.firstinspires.ftc.teamcode.control.PIDFController

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] object from the OpMode
 * @param isVerbose whether to print debug information
 */
@Config
class Lift(hardwareMap: HardwareMap, private val isVerbose: Boolean = true) : Action {
    companion object {
        @JvmField
        @Volatile
        var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

        @JvmField
        @Volatile
        var kStatic = 0.0
        @JvmField
        @Volatile
        var kV = 0.0
        @JvmField
        @Volatile
        var kA = 0.0

        @JvmField
        @Volatile
        var height = 100.0
    }

    enum class SimpleState {
        DOWN, UP
    }

    private enum class State {
        DOWN {
            override val simpleState = SimpleState.DOWN
        },
        GOING_DOWN {
            override val simpleState = SimpleState.DOWN
        },
        UP {
            override val simpleState = SimpleState.UP
        },
        GOING_UP {
            override val simpleState = SimpleState.UP
        };

        abstract val simpleState: SimpleState
    }

    private var state = State.DOWN

    private val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    private val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    val encoder = RawEncoder(liftRight)

    private val lift = listOf(liftLeft, liftRight)

    private val controller = PIDFController(coefficients, kV, kA, kStatic)

    private val profile = constantProfile(height, 0.0, 400.0, -100.0, 100.0).baseProfile

    /**
     * Actual position of the lift, as measured while the action is running
     */
    var measuredPosition = 0.0
        private set

    /**
     * Measured velocity of the lift, as measured while the action is running
     */
    var measuredVelocity = 0.0
        private set

    init {
        lift.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
        encoder.direction = DcMotorSimple.Direction.REVERSE
    }

    var power = 0.0

    override fun run(p: TelemetryPacket): Boolean {
        if (state == State.GOING_DOWN && measuredPosition < 10.0) {
            state = State.DOWN
        } else if (state == State.GOING_UP && measuredPosition > height - 10.0) {
            state = State.UP
        }

        val positionVelocityPair = encoder.getPositionAndVelocity()
        measuredPosition = positionVelocityPair.position.toDouble()
        measuredVelocity = positionVelocityPair.velocity.toDouble()

        val (targetPosition, targetVelocity, targetAcceleration) = when (state.simpleState) {
            SimpleState.UP -> Triple(
                profile[measuredPosition][0],
                profile[measuredPosition][1],
                profile[measuredPosition][2]
            )

            SimpleState.DOWN -> Triple(
                height - profile[height - measuredPosition][0],
                -(height - profile[height - measuredPosition][1]),
                -(height - profile[height - measuredPosition][2])
            )
        }

        controller.targetPosition = targetPosition
        controller.targetVelocity = targetVelocity
        controller.targetAcceleration = targetAcceleration

        if (isVerbose) {
            p.putAll(
                mapOf(
                    "liftPosition" to measuredPosition,
                    "liftVelocity" to measuredVelocity,
                    "liftPower" to power
                )
            )
        }

//        for (motor in lift) {
//            motor.power = power
//        }

        return true
    }

    /**
     * Makes the lift go up if it is currently down
     *
     * Otherwise, does nothing
     */
    fun goUp() {
        if (state == State.DOWN) {
            state = State.GOING_UP
        }
    }

    /**
     * Makes the lift go down if it is currently up
     *
     * Otherwise, does nothing
     */
    fun goDown() {
        if (state == State.UP) {
            state = State.GOING_DOWN
        }
    }

    var targetPosition
        get() = state.simpleState
        set(value) {
            when (value) {
                SimpleState.UP -> goUp()
                SimpleState.DOWN -> goDown()
            }
        }
}