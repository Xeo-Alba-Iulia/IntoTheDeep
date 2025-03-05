package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.core.units.ms
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition

@Config
private class IntakePendulPositions private constructor() {
    companion object {
        @JvmField var pickupWait = 0.4

        @JvmField var pickup = 0.2

        @JvmField var transfer = 0.8
    }
}

object IntakePendul : Subsystem() {
    private lateinit var servo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["IntakePendul"]
    }

    val goPickupWait
        get() = ServoToPosition(servo, IntakePendulPositions.pickupWait, this)
    val goTransfer
        get() = ServoToPosition(servo, IntakePendulPositions.transfer, this)
    val goPickup
        get() =
            if (servo.position == IntakePendulPositions.pickupWait) {
                SequentialGroup(
                    ServoToPosition(servo, IntakePendulPositions.pickup, setOf(this, IntakeClawRotate)),
                    Delay(100.ms),
                )
            } else {
                NullCommand()
            }
}
