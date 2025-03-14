package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.systems.Intake
import org.firstinspires.ftc.teamcode.systems.IntakePositions

@TeleOp
class SensorTest : OpMode() {
    private lateinit var sensor: Sensor
    private lateinit var telemetry: Telemetry
    private lateinit var intake: Intake

    override fun init() {
        sensor = Sensor(hardwareMap)
        telemetry =
            MultipleTelemetry(
                super.telemetry,
                FtcDashboard.getInstance().telemetry,
            )
        intake = Intake(hardwareMap)
    }

    private val pickupTimer = Timer()
    private var isInPickup = false

    override fun loop() {
        if (!isInPickup) {
            intake.targetPosition = IntakePositions.SEARCH
        }

        if (gamepad1.right_bumper) {
            isInPickup = true
            pickupTimer.resetTimer()
            intake.pickUp()
        }
        if (isInPickup && pickupTimer.elapsedTimeSeconds >= 0.3) {
            isInPickup = false
        }
        if (gamepad1.left_bumper) {
            intake.claw.isClosed = false
        }

        telemetry.addData("Holding Sample", sensor.isHoldingSample)
        telemetry.addData("Hovering Sample", sensor.isHoveringSample)

        telemetry.addData("isRed", sensor.isRed)
        telemetry.addData("isBlue", sensor.isBlue)
        telemetry.addData("isYellow", sensor.isYellow)

        telemetry.addData("Red", sensor.red())
        telemetry.addData("Blue", sensor.blue())
        telemetry.addData("Green", sensor.green())

        val p = TelemetryPacket()
        intake.run(p)
        FtcDashboard.getInstance().sendTelemetryPacket(p)
    }
}
