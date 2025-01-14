package org.firstinspires.ftc.teamcode.systems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.ClawRotate
import org.firstinspires.ftc.teamcode.systems.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions

class Outtake(hardwareMap: HardwareMap) : Action {
    val rotation = ClawRotate(hardwareMap)
    val pendul = Pendul(hardwareMap)

    override fun run(p: TelemetryPacket) =rotation.run(p) && pendul.run(p)

    var outtakePosition: OuttakePosition
        get() = when (pendul.targetPosition) {
            Positions.Pendul.transfer -> OuttakePosition.TRANSFER
            Positions.Pendul.outtake -> OuttakePosition.OUTTAKE
            else -> OuttakePosition.BASKET
        }
        set(value) {
            when (value) {
                OuttakePosition.TRANSFER -> {
                    rotation.targetPosition = Positions.ClawRotate.transfer
                    pendul.targetPosition = Positions.Pendul.transfer
                }
                OuttakePosition.BASKET -> {
                    rotation.targetPosition = Positions.ClawRotate.basket
                    pendul.targetPosition = Positions.Pendul.basket
                }
                OuttakePosition.OUTTAKE -> {
                    rotation.targetPosition = Positions.ClawRotate.outtake
                    pendul.targetPosition = Positions.Pendul.outtake
                }
                OuttakePosition.SMASH -> {
                    rotation.targetPosition = Positions.ClawRotate.outtake
                    pendul.targetPosition = Positions.Pendul.smash
                }
            }

        }
}

enum class OuttakePosition {
    TRANSFER,
    SMASH,
    BASKET,
    OUTTAKE
}

@TeleOp(name = "Outtake positions Test", group = "B")
class OuttakeTest : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()
        val outtake = Outtake(hardwareMap)

        while (opModeIsActive()) {
            outtake.outtakePosition = when {
                gamepad1.a -> OuttakePosition.TRANSFER
                gamepad1.b -> OuttakePosition.BASKET
                gamepad1.x -> OuttakePosition.OUTTAKE
                gamepad1.y -> OuttakePosition.SMASH
                else -> outtake.outtakePosition
            }

            val packet = TelemetryPacket()
            outtake.run(packet)
            telemetry.addData("Intake Position", outtake.outtakePosition)
            telemetry.update()
        }
    }
}