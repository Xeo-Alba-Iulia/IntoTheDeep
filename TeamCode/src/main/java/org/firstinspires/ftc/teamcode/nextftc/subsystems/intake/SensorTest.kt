package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import org.firstinspires.ftc.teamcode.nextftc.Intake

@TeleOp
class SensorTest : NextFTCOpMode(SubsystemGroup(Sensor, IntakeClaw, Intake)) {
    lateinit var telemetry: MultipleTelemetry
    lateinit var servo: Servo

    override fun onInit() {
        telemetry =
            MultipleTelemetry(
                super.telemetry,
                FtcDashboard.getInstance().telemetry,
            )

        SequentialGroup(
            Intake.toPickupWait,
            IntakePendul.toSearch,
        )()
    }

    private var doAssert = false

    override fun onStartButtonPressed() {
        gamepadManager.gamepad1.cross.pressedCommand = { IntakeClaw.toggle }
        gamepadManager.gamepad1.circle.pressedCommand = { InstantCommand { doAssert = !doAssert } }
        gamepadManager.gamepad1.square.pressedCommand = { Intake.pickup }
    }

    override fun onUpdate() {
        if (doAssert) {
            assert(!Sensor.isHoldingSample)
        }
        telemetry.addData("isHoldingSample", Sensor.isHoldingSample)
        telemetry.addData("isHoveringSample", Sensor.isHoveringSample)
        telemetry.addData("isRed", Sensor.isRed)
        telemetry.addData("isBlue", Sensor.isBlue)
        telemetry.addData("isYellow", Sensor.isYellow)

        telemetry.addData("Red Value", Sensor.red())
        telemetry.addData("Green Value", Sensor.green())
        telemetry.addData("Blue Value", Sensor.blue())
        telemetry.addData("Servo Value", Sensor.voltage)

        telemetry.update()
    }
}
