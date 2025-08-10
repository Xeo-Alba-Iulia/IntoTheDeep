package org.firstinspires.ftc.teamcode.dagger

import dagger.Component
import org.firstinspires.ftc.teamcode.dagger.DaggerInjectedOpMode
import org.firstinspires.ftc.teamcode.dagger.TestModule

@Component(modules = [TestModule::class])
interface DaggerTestComponent : BaseOpModeComponent<DaggerInjectedOpMode> {
    @Component.Builder
    interface Builder : BaseOpModeComponent.Builder<DaggerInjectedOpMode>
}
