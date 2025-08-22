package org.firstinspires.ftc.teamcode.systems

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.*
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import javax.inject.Inject

class Outtake
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) : Action {
        val rotation = ClawRotate(hardwareMap)
        val pendul = Pendul(hardwareMap)
        val outtakeRotation = OuttakeRotate(hardwareMap)
        val claw = Claw(hardwareMap)

        override fun run(p: TelemetryPacket) =
            rotation.run(p) && pendul.run(p) && claw.run(p) && outtakeRotation.run(p)

        var outtakePosition: OuttakePosition = OuttakePosition.TRANSFER
            set(value) {
                field = value
                when (value) {
                    OuttakePosition.TRANSFER -> {
                        rotation.targetPosition = Positions.ClawRotate.transfer
                        pendul.targetPosition = Positions.Pendul.transfer
                        outtakeRotation.targetPosition = Positions.OuttakeRotate.up
                    }

                    OuttakePosition.BASKET -> {
                        rotation.targetPosition = Positions.ClawRotate.basket
                        pendul.targetPosition = Positions.Pendul.basket
                        outtakeRotation.targetPosition = Positions.OuttakeRotate.up
                    }

                    OuttakePosition.BAR -> {
                        rotation.targetPosition = Positions.ClawRotate.bar
                        pendul.targetPosition = Positions.Pendul.bar
                        outtakeRotation.targetPosition = Positions.OuttakeRotate.up
                    }

                    OuttakePosition.PICKUP -> {
                        rotation.targetPosition = Positions.ClawRotate.pickup
                        pendul.targetPosition = Positions.Pendul.pickup
                        outtakeRotation.targetPosition = Positions.OuttakeRotate.down
                    }
                }
            }
    }

enum class OuttakePosition {
    TRANSFER,
    BASKET,
    BAR,
    PICKUP,
}

@TeleOp(group = "B")
class OuttakeTest : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()
        val outtake = Outtake(hardwareMap)
        val dashboard = FtcDashboard.getInstance()

        while (opModeIsActive()) {
            outtake.outtakePosition =
                when {
                    gamepad1.cross -> OuttakePosition.TRANSFER
                    gamepad1.circle -> OuttakePosition.BASKET
                    gamepad1.triangle -> OuttakePosition.BAR
                    gamepad1.square -> OuttakePosition.PICKUP
                    else -> outtake.outtakePosition
                }

            val packet = TelemetryPacket()
            outtake.run(packet)
            telemetry.addData("Intake Position", outtake.outtakePosition)
            dashboard.sendTelemetryPacket(packet)
            telemetry.update()
        }
    }
}
