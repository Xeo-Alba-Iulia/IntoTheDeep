package org.firstinspires.ftc.teamcode.opmodes.audio

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.EventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier
import com.qualcomm.robotcore.util.RobotLog
import fi.iki.elonen.NanoHTTPD
import org.firstinspires.ftc.ftccommon.external.OnCreate
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop

object VoiceAssistant : OpModeManagerNotifier.Notifications {
    const val TAG = "VoiceAssistantInit"

    lateinit var eventLoop: EventLoop

    @SuppressLint("StaticFieldLeak")
    lateinit var opModeManager: OpModeManagerImpl

    var opMode: OpMode? = null

    lateinit var nanoHTTPD: NanoHTTPD

    @JvmStatic
    @OnCreate
    fun onCreate(context: Context) {
        nanoHTTPD = VoiceAssistantServer()
        nanoHTTPD.start()
    }

    @JvmStatic
    @OnCreateEventLoop
    fun onCreateEventLoop(
        context: Context,
        ftcEventLoop: FtcEventLoop,
    ) {
        if (::eventLoop.isInitialized) {
            throw IllegalStateException("onCreateEventLoop called more than once")
        }
        this.eventLoop = ftcEventLoop
        this.opModeManager = eventLoop.opModeManager

        opModeManager.registerListener(this)
    }

    override fun onOpModePreStart(opMode: OpMode) {
        RobotLog.dd(TAG, "OpMode $opMode started")
        this.opMode = opMode
    }

    override fun onOpModePostStop(opMode: OpMode) {
        RobotLog.dd(TAG, "OpMode $opMode stopped")
        this.opMode = null
    }

    override fun onOpModePreInit(opMode: OpMode?) {}
}

private class VoiceAssistantServer : NanoHTTPD(5000) {
    companion object {
        const val TAG = "VoiceAssistantServer"
    }

    private val gson = Gson()

    override fun serve(session: IHTTPSession): Response {
        RobotLog.dd(TAG, "Received request")
        val uriIterator = session.uri.split('/').iterator()
        uriIterator.next()
        val op = uriIterator.next()

        if (op == "init") {
            VoiceAssistant.opModeManager.initOpMode(uriIterator.next())
            return newFixedLengthResponse("OpMode Started successfully")
        }

        val opMode = VoiceAssistantOpMode.instance

        if (opMode == null) {
            return newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                "text/plain",
                "OpMode isn't running",
            )
        }

        when (op) {
            "setPower" -> opMode.setPower(uriIterator.next().toDouble())

            else -> return newFixedLengthResponse(
                Response.Status.NOT_FOUND,
                "text/plain",
                "Unknown operation: $op",
            )
        }

        return newFixedLengthResponse("Success")
    }
}
