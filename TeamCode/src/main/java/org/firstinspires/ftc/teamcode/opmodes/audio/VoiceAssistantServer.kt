package org.firstinspires.ftc.teamcode.opmodes.audio

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.EventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier
import dev.frozenmilk.sinister.loading.Preload
import dev.frozenmilk.sinister.sdk.apphooks.OnCreate
import dev.frozenmilk.sinister.sdk.apphooks.OnCreateEventLoop
import fi.iki.elonen.NanoHTTPD
import java.lang.reflect.Parameter

data class OpModeName(
    val opModeName: String,
)

private object VoiceAssistantOnCreateEventLoop : OnCreateEventLoop {
    override fun onCreateEventLoop(
        context: Context,
        ftcEventLoop: FtcEventLoop,
    ) {
        VoiceAssistantInit.onCreateEventLoop(ftcEventLoop)
    }
}

private object VoiceAssistantOnCreate : OnCreate {
    override fun onCreate(context: Context) {
        VoiceAssistantInit.onCreate()
    }
}

@Preload
object VoiceAssistantInit : OpModeManagerNotifier.Notifications {
    val TAG = this::class.simpleName as String

    lateinit var eventLoop: EventLoop

    @SuppressLint("StaticFieldLeak")
    lateinit var opModeManager: OpModeManagerImpl

    var opMode: OpMode? = null

    lateinit var nanoHTTPD: NanoHTTPD

    fun onCreate() {
        nanoHTTPD = VoiceAssistantServer()
        nanoHTTPD.start()
    }

    fun onCreateEventLoop(ftcEventLoop: FtcEventLoop) {
        if (::eventLoop.isInitialized) {
            throw IllegalStateException("onCreateEventLoop called more than once")
        }
        this.eventLoop = ftcEventLoop
        this.opModeManager = eventLoop.opModeManager

        opModeManager.registerListener(this)
    }

    override fun onOpModePreStart(opMode: OpMode) {
        this.opMode = opMode
    }

    override fun onOpModePostStop(opMode: OpMode) {
        this.opMode = null
    }

    override fun onOpModePreInit(opMode: OpMode?) {}
}

private class VoiceAssistantServer : NanoHTTPD(8080) {
    private val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.trim('/')
        val body = session.inputStream.bufferedReader().readText()

        if (uri == "init") {
            val opModeName = gson.fromJson(body, OpModeName::class.java).opModeName
            VoiceAssistantInit.opModeManager.initOpMode(opModeName)
            return newFixedLengthResponse("OpMode Started successfully")
        }

        if (VoiceAssistantInit.opMode == null) {
            return newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                "text/plain",
                "OpMode isn't running",
            )
        }

        val method =
            VoiceAssistantInit.opMode?.javaClass?.declaredMethods?.find {
                it.isAnnotationPresent(VoiceActivated::class.java) && it.name == uri
            } ?: return newFixedLengthResponse(
                Response.Status.NOT_FOUND,
                "text/plain",
                "Method not found",
            )

        val jsonObject =
            JsonParser().parse(body).asJsonObject ?: return newFixedLengthResponse(
                Response.Status.BAD_REQUEST,
                "text/plain",
                "Invalid JSON body",
            )

        method.invoke(
            VoiceAssistantInit.opMode,
            *method.parameters
                .map { parameter: Parameter ->
                    val name = parameter.name
                    val type = parameter.type

                    gson.fromJson(jsonObject[name], type)
                }.toTypedArray(),
        )

        return newFixedLengthResponse("Operation executed successfully")
    }
}
