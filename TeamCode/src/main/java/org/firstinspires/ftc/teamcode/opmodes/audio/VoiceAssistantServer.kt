package org.firstinspires.ftc.teamcode.opmodes.audio

import com.qualcomm.robotcore.util.RobotLog
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoWSD
import fi.iki.elonen.NanoWSD.WebSocket
import org.firstinspires.ftc.ftccommon.external.OnCreate
import java.io.IOException
import kotlin.reflect.KCallable
import kotlin.reflect.full.valueParameters

class VoiceAssistantServer {
    companion object {
        const val TAG = "VoiceAssistantServer"
        private const val PORT = 5000

        lateinit var instance: VoiceAssistantServer
            private set

        @OnCreate
        @JvmStatic
        fun init() {
            if (!::instance.isInitialized) {
                instance = VoiceAssistantServer()
            }
        }
    }

    private val map: MutableMap<String, KCallable<*>> = mutableMapOf()

    init {
        createFunctionMap()
    }

    private val wsd =
        object : NanoWSD(PORT) {
            override fun openWebSocket(handshake: IHTTPSession) = VoiceAssistantSocket(handshake, map)
        }

    init {
        wsd.start()
    }

    private fun createFunctionMap() {
        for (callable in VoiceAssistantOpMode::class.members) {
            if (callable.annotations.any { it is VoiceActivated }) {
                require(callable.valueParameters.isEmpty()) { "Annotated function takes parameters" }
                map[callable.name] = callable
            }
        }
    }

    private class VoiceAssistantSocket(
        handshake: IHTTPSession,
        val map: Map<String, KCallable<*>>,
    ) : WebSocket(handshake) {
        override fun onOpen() {
            RobotLog.ii(TAG, "WebSocket opened")
        }

        override fun onClose(
            closeCode: NanoWSD.WebSocketFrame.CloseCode?,
            reason: String?,
            closedByRemote: Boolean,
        ) {
            if (closeCode != NanoWSD.WebSocketFrame.CloseCode.NormalClosure) {
                RobotLog.ww(
                    TAG,
                    "WebSocket closed with code: $closeCode, reason: $reason, closed by remote: $closedByRemote",
                )
            }
        }

        override fun onMessage(frame: NanoWSD.WebSocketFrame) {
            val message = frame.textPayload
            RobotLog.dd(TAG, "WebSocket message received: $message")
            if (VoiceAssistantOpMode.instance?.isInLoop != true) {
                send("Voice Assistant Op Mode is not running")
                return
            }
            val operation = map[message]
            if (operation == null) {
                send("Requested Operation can't be found")
                RobotLog.ww(TAG, "Wrong function call in message: $message")
                return
            }
            operation.call(instance)
        }

        override fun onPong(frame: NanoWSD.WebSocketFrame) {
            RobotLog.dd(TAG, "WebSocket pong received: ${frame.textPayload}")
        }

        override fun onException(exception: IOException) {
        }
    }
}
