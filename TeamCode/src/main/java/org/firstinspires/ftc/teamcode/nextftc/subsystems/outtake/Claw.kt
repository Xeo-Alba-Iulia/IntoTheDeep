package org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.core.units.ms
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions

@Config
private class ClawPositions private constructor() {
    companion object {
        @JvmField var open = 0.5

        @JvmField var close = 0.654
    }
}

object Claw : Subsystem() {
    private lateinit var servo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["IntakeClaw"]
    }

    val isClosed get() = servo.position == Positions.Claw.close

    val open get() =
        SequentialGroup(
            ServoToPosition(servo, ClawPositions.open, this),
            Delay(80.ms),
        )
    val close get() =
        SequentialGroup(
            ServoToPosition(servo, ClawPositions.close, this),
            Delay(80.ms),
        )

    val toggle get() = if (isClosed) open else close
}
