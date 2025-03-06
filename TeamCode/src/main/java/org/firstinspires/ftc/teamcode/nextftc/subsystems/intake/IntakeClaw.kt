package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.core.units.ms
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition

@Config
private class IntakeClawPositions private constructor() {
    companion object {
        @JvmField var open = 0.46

        @JvmField var close = 0.79
    }
}

object IntakeClaw : Subsystem() {
    private lateinit var servo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["IntakeClaw"]
    }

    val isClosed get() = servo.position == IntakeClawPositions.close

    val open get() =
        SequentialGroup(
            ServoToPosition(servo, IntakeClawPositions.open, this),
            Delay(80.ms),
        )
    val close get() =
        SequentialGroup(
            ServoToPosition(
                servo,
                IntakeClawPositions.close,
                setOf(
                    this,
                    IntakeClawRotate,
                    Extend,
                    IntakePendul,
                ),
            ),
            Delay(80.ms),
        )

    val toggle get() = if (isClosed) open else close
}
