package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class Extend(hardwareMap: HardwareMap) : Action {
    private val extendLeft = hardwareMap.crservo.get("ExtendLeft")
    private val extendRight = hardwareMap.crservo.get("ExtendRight")

    init {
        extendLeft.direction = DcMotorSimple.Direction.REVERSE
    }

    val motors = arrayListOf(extendLeft, extendRight)

    var power = 0.0

    override fun run(p: TelemetryPacket): Boolean {
        motors.forEach { it.power = power }

        return true
    }
}