package org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData.hardwareMap
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition

@Config
private class PendulPositions private constructor() {
    companion object {
        @JvmField @Volatile
        var transfer = 0.19

        @JvmField @Volatile
        var basket = 0.58

        @JvmField @Volatile
        var bar = 0.71

        @JvmField @Volatile
        var pickup = 0.834
    }
}

object Pendul : Subsystem() {
    lateinit var servos: List<Servo>

    override fun initialize() {
        servos =
            listOf(
                hardwareMap.servo["Pendul1"],
                hardwareMap.servo["Pendul2"],
            )
    }

    val goTransfer get() = MultipleServosToPosition(servos, PendulPositions.transfer, this)
    val goPickup get() = MultipleServosToPosition(servos, PendulPositions.pickup, this)
    val goBar get() = MultipleServosToPosition(servos, PendulPositions.bar, this)
    val goBasket get() = MultipleServosToPosition(servos, PendulPositions.basket, this)
}
