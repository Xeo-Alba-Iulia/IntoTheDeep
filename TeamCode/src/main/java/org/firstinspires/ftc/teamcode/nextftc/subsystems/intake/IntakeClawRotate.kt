package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition

@Config
private class IntakeClawRotatePositions private constructor() {
    companion object {
        @JvmField var left = 0.265

        @JvmField var middle = 0.525

        @JvmField var right = 0.785
    }
}

object IntakeClawRotate : Subsystem() {
    private lateinit var servo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["IntakeClawRotate"]
    }

    private fun goToPosition(position: Double) = ServoToPosition(servo, position, this)

    val goLeft get() = goToPosition(IntakeClawRotatePositions.left)
    val goMiddle get() = goToPosition(IntakeClawRotatePositions.middle)
    val goRight get() = goToPosition(IntakeClawRotatePositions.right)
}
