package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.HardwareMap
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
internal object TestModule {
    @Provides
    fun provideRobot() = RobotHardware(Any() as HardwareMap)
}
