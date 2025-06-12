package org.firstinspires.ftc.teamcode.opmodes.audio

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.systems.subsystems.Movement

@TeleOp
class VoiceAssistantOpMode : OpMode() {
    companion object {
        var instance: VoiceAssistantOpMode? = null
            private set
    }

    private lateinit var motor: DcMotor

    var isInLoop = false
        private set

    override fun init() {
        if (instance != null) {
            throw IllegalStateException("VoiceAssistantOpMode already initialized")
        }
        instance = this

        motor = Movement(hardwareMap).frontLeft
    }

    override fun start() {
        isInLoop = true
    }

    override fun loop() {}

    override fun stop() {
        isInLoop = false
        instance = null
    }

    @VoiceActivated
    fun setPower(power: Double) {
        motor.power = power
    }
}
