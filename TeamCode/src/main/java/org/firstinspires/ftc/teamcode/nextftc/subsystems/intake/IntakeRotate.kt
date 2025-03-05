package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData.hardwareMap
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition

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

object IntakeRotate : Subsystem() {
    lateinit var servo: Servo

    override fun initialize() {
        servo = hardwareMap.servo["IntakeRotate"]
    }

    val goTransfer get() = ServoToPosition(servo, IntakeRotatePositions.transfer, this)
    val goPickupWait get() = ServoToPosition(servo, IntakeRotatePositions.pickupWait, this)
    val goPickup get() = ServoToPosition(servo, IntakeRotatePositions.pickup, this)
}
