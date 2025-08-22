package org.firstinspires.ftc.teamcode.dagger

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.subsystems.Lift
import javax.inject.Inject

abstract class InjectTest : OpMode() {
    @Inject
    lateinit var shouldPass: RobotHardware

    @Inject
    lateinit var lift: Lift

    protected val component =
        DaggerOpModeComponent
            .builder()
            .opMode(this)
            .build()

    override fun init() {
        checkNotNull((this as OpMode).hardwareMap)
        component.inject(this)
    }
}
