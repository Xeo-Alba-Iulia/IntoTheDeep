package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor = hardwareMap.crservo.get("IntakeMotor")

    val runUntilCaught = object : Action {
        private var isCanceled = false

        fun cancel() {
            isCanceled = true
        }

        override fun run(p: TelemetryPacket): Boolean {
            if (isCanceled) {
                intakeMotor.power = 0.0
                return false
            }
            intakeMotor.power = 0.1

            return true
        }
    }
}