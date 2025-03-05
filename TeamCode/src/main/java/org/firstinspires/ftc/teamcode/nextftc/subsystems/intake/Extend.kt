package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData.hardwareMap
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition

@Config
private class ExtendPositions private constructor() {
    companion object {
        @JvmField var extendPosition = 0.933

        @JvmField var retractPosition = 0.56
    }
}

@Config
object Extend : Subsystem() {
    private lateinit var servos: List<Servo>

    override fun initialize() {
        servos =
            listOf(
                hardwareMap.servo["Extend1"],
                hardwareMap.servo["Extend2"],
            )
    }

    val extend get() = MultipleServosToPosition(servos, ExtendPositions.extendPosition, this)
    val retract get() = MultipleServosToPosition(servos, ExtendPositions.retractPosition, this)
}
