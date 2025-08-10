package org.firstinspires.ftc.teamcode.dagger

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import javax.inject.Inject

abstract class DaggerInjectedOpMode : OpMode() {
    @Inject
    lateinit var shouldPass: RobotHardware

    protected val component by lazy {
        DaggerDaggerTestComponent
            .builder()
            .hardwareMap(hardwareMap)
            .telemetry(telemetry)
            .opMode(this)
            .build()
    }

    override fun init() {
        component.inject(this)
    }
}
