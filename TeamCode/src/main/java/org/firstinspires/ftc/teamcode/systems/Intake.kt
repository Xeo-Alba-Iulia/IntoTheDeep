package org.firstinspires.ftc.teamcode.systems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.IntakePositions.PICKUP
import org.firstinspires.ftc.teamcode.systems.IntakePositions.TRANSFER
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.Extend
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.IntakePendul
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions

class Intake(
    hardwareMap: HardwareMap,
) : Action {
    val pendul = IntakePendul(hardwareMap)
    val extend = Extend(hardwareMap)

    override fun run(p: TelemetryPacket) = pendul.run(p) && extend.run(p)

    var targetPosition: IntakePositions = PICKUP
        set(value) =
            when (value) {
                PICKUP -> {
                    pendul.targetPosition = Positions.IntakePendul.transfer
                    extend.targetPosition = Positions.Extend.`in`
                }

                TRANSFER -> {
                    pendul.targetPosition = Positions.IntakePendul.pickup
                    extend.targetPosition = Positions.Extend.out
                }
            }
}
