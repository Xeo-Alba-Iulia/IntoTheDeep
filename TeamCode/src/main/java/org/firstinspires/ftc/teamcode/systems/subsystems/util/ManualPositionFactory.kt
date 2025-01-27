package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.qualcomm.robotcore.hardware.HardwareMap

fun interface ManualPositionFactory {
    fun manualPositionFactory(hardwareMap: HardwareMap): ManualPositionMechanism
}
