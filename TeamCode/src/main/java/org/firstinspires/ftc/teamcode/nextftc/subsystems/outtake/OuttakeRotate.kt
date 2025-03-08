package org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition

private class OuttakeRotatePositions private constructor() {
    companion object {
        @JvmField @Volatile
        var down = 0.1

        @JvmField @Volatile
        var up = 0.8
    }
}

object OuttakeRotate : Subsystem() {
    private lateinit var servo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["OuttakeRotate"]
    }

    val toDown get() = ServoToPosition(servo, OuttakeRotatePositions.down, this)
    val toUp get() = ServoToPosition(servo, OuttakeRotatePositions.up, this)
}
