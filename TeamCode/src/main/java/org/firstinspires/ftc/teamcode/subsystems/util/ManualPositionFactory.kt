package org.firstinspires.ftc.teamcode.subsystems.util

import com.qualcomm.robotcore.hardware.HardwareMap

fun interface ManualPositionFactory {
    fun manualPositionFactory(hardwareMap: HardwareMap): ManualPositionMechanism
}