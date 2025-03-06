package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.core.units.ms
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToSeperatePositions

@Config
private class IntakePendulPositions private constructor() {
    companion object {
        @JvmField var pickupWait = 0.4

        @JvmField var pickup = 0.2

        @JvmField var transfer = 0.8
    }
}

@Config
private class IntakeRotatePositions private constructor() {
    companion object {
        @JvmField @Volatile
        var pickupWait = 0.1

        @JvmField @Volatile
        var pickup = 0.05

        @JvmField @Volatile
        var transfer = 0.75
    }
}

object IntakePendul : Subsystem() {
    private lateinit var servo: Servo
    private lateinit var rotateServo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["IntakePendul"]
        rotateServo = OpModeData.hardwareMap.servo["IntakeRotate"]
    }

    val goPickupWait
        get() =
            MultipleServosToSeperatePositions(
                mapOf(
                    servo to IntakePendulPositions.pickupWait,
                    rotateServo to IntakeRotatePositions.pickupWait,
                ),
                this,
            )
    val goTransfer
        get() =
            MultipleServosToSeperatePositions(
                mapOf(
                    servo to IntakePendulPositions.transfer,
                    rotateServo to IntakeRotatePositions.transfer,
                ),
                this,
            )
    val goPickup
        get() =
            if (servo.position == IntakePendulPositions.pickupWait) {
                SequentialGroup(
                    MultipleServosToSeperatePositions(
                        mapOf(
                            servo to IntakePendulPositions.pickup,
                            rotateServo to IntakeRotatePositions.pickup,
                        ),
                        setOf(
                            this,
                            IntakeClaw,
                            IntakeClawRotate,
                            Extend,
                        ),
                    ),
                    Delay(100.ms),
                )
            } else {
                NullCommand()
            }
}
