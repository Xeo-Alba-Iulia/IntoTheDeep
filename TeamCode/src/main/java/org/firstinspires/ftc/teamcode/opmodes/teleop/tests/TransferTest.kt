package org.firstinspires.ftc.teamcode.opmodes.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakePendul
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.ClawRotate
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import pedroPathing.examples.ExampleRobotCentricTeleop

@TeleOp
class TransferTest : ExampleRobotCentricTeleop() {
    lateinit var intakePendul: IntakePendul
    lateinit var pendul: Pendul
    lateinit var clawRotate: ClawRotate

    private lateinit var dashboard: FtcDashboard

    override fun init() {
        super.init()

        intakePendul = IntakePendul(hardwareMap)
        pendul = Pendul(hardwareMap)
        clawRotate = ClawRotate(hardwareMap)
        dashboard = FtcDashboard.getInstance()
    }

    override fun loop() {
        val packet = TelemetryPacket()
        require(pendul.run(packet) && intakePendul.run(packet) && clawRotate.run(packet)) {
            "Nu ar trebui sa se opreasca vreun mecanism singur"
        }

        when {
            gamepad1.cross -> {
                intakePendul.targetPosition = Positions.IntakePendul.transfer
                pendul.targetPosition = Positions.Pendul.transfer
                clawRotate.targetPosition = Positions.ClawRotate.transfer
            }

            gamepad1.circle -> {
                intakePendul.targetPosition = Positions.IntakePendul.pickup
                pendul.targetPosition = Positions.Pendul.bar
                clawRotate.targetPosition = Positions.ClawRotate.bar
            }
        }

        dashboard.sendTelemetryPacket(packet)
        super.loop()
    }
}
