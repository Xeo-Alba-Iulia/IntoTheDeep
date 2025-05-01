package org.firstinspires.ftc.teamcode.opmodes.audio

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class VoiceAssistantOpMode : OpMode() {
    companion object {
        var instance: VoiceAssistantOpMode? = null
            private set
    }

    public var isInLoop = false
        private set

    override fun init() {
        TODO("Actual initialization code")

        if (instance != null) {
            throw IllegalStateException("VoiceAssistantOpMode already initialized")
        }
        instance = this
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
    fun goForward(distance: Double) {
    }
}
