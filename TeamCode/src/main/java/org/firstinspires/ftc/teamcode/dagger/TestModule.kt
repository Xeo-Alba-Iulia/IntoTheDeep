package org.firstinspires.ftc.teamcode.dagger

import com.qualcomm.robotcore.hardware.HardwareMap
import dagger.Module
import dagger.Provides
import org.firstinspires.ftc.teamcode.RobotHardware

@Module
internal object TestModule {
    @Suppress("KotlinConstantConditions")
    @Provides
    fun provideRobot(hardwareMap: HardwareMap) = RobotHardware(hardwareMap)
}
