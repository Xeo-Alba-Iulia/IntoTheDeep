package org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData.hardwareMap
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition

@Config
private class ClawRotatePositions private constructor() {
    companion object {
        @JvmField @Volatile
        var transfer = 0.83

        @JvmField @Volatile
        var bar = 0.73

        @JvmField @Volatile
        var basket = 0.27

        @JvmField @Volatile
        var pickup = 0.61
    }
}

object ClawRotate : Subsystem() {
    lateinit var servo: Servo

    override fun initialize() {
        servo = hardwareMap.servo["ClawRotate"]
    }

    val goTransfer get() = ServoToPosition(servo, ClawRotatePositions.transfer, this)
    val goBar get() = ServoToPosition(servo, ClawRotatePositions.bar, this)
    val goBasket get() = ServoToPosition(servo, ClawRotatePositions.basket, this)
    val goPickup get() = ServoToPosition(servo, ClawRotatePositions.pickup, this)
}
