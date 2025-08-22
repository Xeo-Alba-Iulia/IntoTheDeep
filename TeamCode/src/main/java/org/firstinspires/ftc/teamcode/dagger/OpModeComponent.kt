package org.firstinspires.ftc.teamcode.dagger

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import dagger.BindsInstance
import dagger.Component
import org.firstinspires.ftc.teamcode.opmodes.teleop.MainTeleOp
import javax.inject.Singleton

@Component(modules = [TestModule::class, OpModeModule::class])
@Singleton
@OpModeScope
interface OpModeComponent {
    fun inject(opMode: MainTeleOp)

    fun inject(opMode: InjectTest)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun opMode(opMode: OpMode): Builder

        fun build(): OpModeComponent
    }
}
