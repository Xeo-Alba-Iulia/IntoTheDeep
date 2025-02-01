package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.DeviceNames

/**
 * Intake subsystem
 *
 * @param hardwareMap the [HardwareMap] object from the OpMode
 */
class IntakeMotor(
    hardwareMap: HardwareMap,
) : Action {
    private val motors: Array<CRServo> = arrayOf(
        hardwareMap.crservo[DeviceNames.ILMotor],
        hardwareMap.crservo[DeviceNames.IRMotor]
    )

//    init {
//        motors[0].direction = DcMotorSimple.Direction.REVERSE
//    }

    // Implementing Action interface for the actual Intake

    /**
     * Power to set the intake to
     */
    var intakePower: Double = 0.0
        set(value) {
            require(value in -1.0..1.0) { "Intake power must be between -1 and 1" }
            field = value
        }

    override fun run(p: TelemetryPacket): Boolean {
        motors.forEach { it.power = intakePower }
        p.put("Intake Motor Power", intakePower)
        return true
    }
}

@TeleOp(name = "Intake Motor Test", group = "C")
class IntakeMotorTest : OpMode() {
    private lateinit var intakeMotor: IntakeMotor

    override fun init() {
        intakeMotor = IntakeMotor(hardwareMap)
        val dashboard: FtcDashboard = FtcDashboard.getInstance()
    }

    override fun loop() {
        val packet = TelemetryPacket()

        intakeMotor.intakePower = gamepad1.left_stick_y.toDouble()
        intakeMotor.run(packet)
    }

}
