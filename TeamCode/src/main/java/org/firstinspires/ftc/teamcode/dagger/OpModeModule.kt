package org.firstinspires.ftc.teamcode.dagger

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import dagger.Module
import dagger.Provides

@Module
internal object OpModeModule {
    @Provides
    @OpModeScope
    fun provideHardwareMap(opMode: OpMode) =
        opMode.hardwareMap ?: throw IllegalStateException("Hardware map not initialized")

    @Provides
    @OpModeScope
    fun provideTelemetry(opMode: OpMode) =
        opMode.telemetry ?: throw IllegalStateException("Telemetry not initialized")
}
