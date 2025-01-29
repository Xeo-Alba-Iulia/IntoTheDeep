package org.firstinspires.ftc.teamcode.systems.subsystems

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualPositionMechanism

/**
 * Lift subsystem
 *
 * @param hardwareMap the [HardwareMap] instance from OpMode
 */
@Config
class Lift(
    hardwareMap: HardwareMap,
) : ManualPositionMechanism {
    private val liftLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftLeft")
    private val liftRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "LiftRight")

    private var isCanceled = false

    private val liftMotors = listOf(liftLeft, liftRight)

    init {
        liftMotors.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            it.targetPosition = 0

            it.mode = DcMotor.RunMode.RUN_TO_POSITION
        }

        liftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    var power = 1.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    override var targetPosition = 0.0
        set(value) {
            field = value.coerceIn(0.0, 2000.0)
        }

    override fun cancel() {
        isCanceled = true
    }

    override fun run(p: TelemetryPacket): Boolean {
        if (isCanceled) {
            isCanceled = false
            power = 0.0

            return false
        }

        for (motor in liftMotors) {
            motor.power = power
            motor.targetPosition = targetPosition.toInt()
        }

        p.put("Lift Power", power)
        p.put("Lift Target", targetPosition)

        return true
    }
}

@TeleOp(name = "Lift test", group = "C")
class LiftTest : ManualMechanismTeleOp(::Lift) {
    override val multiplier: Double
        get() = 0.3

    override fun loop() {
        super.loop()
        if (gamepad1.a) {
            manualPositionMechanism.targetPosition = 1000.0
        } else if (gamepad1.b) {
            manualPositionMechanism.targetPosition = 0.0
        }
    }
}
