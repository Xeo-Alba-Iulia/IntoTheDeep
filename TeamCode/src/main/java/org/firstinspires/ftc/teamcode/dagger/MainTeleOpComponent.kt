package org.firstinspires.ftc.teamcode.dagger

import dagger.Component
import org.firstinspires.ftc.teamcode.opmodes.teleop.MainTeleOp

@Component
interface MainTeleOpComponent : BaseOpModeComponent<MainTeleOp> {
    @Component.Builder
    interface Builder : BaseOpModeComponent.Builder<MainTeleOp>
}
