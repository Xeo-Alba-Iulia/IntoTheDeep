package org.firstinspires.ftc.teamcode.dagger

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import dagger.BindsInstance
import dagger.Component
import org.firstinspires.ftc.robotcore.external.Telemetry

interface BaseOpModeComponent<T : OpMode> {
    fun inject(opMode: T)

    interface Builder<T : OpMode> {
        fun hardwareMap(
            @BindsInstance hardwareMap: HardwareMap,
        ): Builder<T>

        fun telemetry(
            @BindsInstance telemetry: Telemetry,
        ): Builder<T>

        fun opMode(
            @BindsInstance opMode: T,
        ): Builder<T>

        fun build(): BaseOpModeComponent<T>
    }
}
