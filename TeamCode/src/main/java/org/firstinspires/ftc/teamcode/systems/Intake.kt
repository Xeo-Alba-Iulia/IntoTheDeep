package org.firstinspires.ftc.teamcode.systems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.IntakePositions.PICKUP
import org.firstinspires.ftc.teamcode.systems.IntakePositions.TRANSFER
import org.firstinspires.ftc.teamcode.systems.subsystems.intake.*
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions

class Intake(
    hardwareMap: HardwareMap,
) : Action {
    val pendul = IntakePendul(hardwareMap)
    val extend = Extend(hardwareMap)
    val clawRotate = IntakeClawRotate(hardwareMap)
    val rotate = IntakeRotate(hardwareMap)
    val claw = IntakeClaw(hardwareMap)

    private var needsPickup = false
    private var pickupTimer = Timer()

    override fun run(p: TelemetryPacket): Boolean {
        if (needsPickup) {
            pendul.targetPosition = Positions.IntakePendul.pickup
        }

        if (needsPickup && pickupTimer.elapsedTimeSeconds >= 0.15) {
            isClosed = true
        }

        if (needsPickup && pickupTimer.elapsedTimeSeconds >= 0.3) {
            pendul.targetPosition = Positions.IntakePendul.pickupWait
            needsPickup = false
        }

        return pendul.run(p) && extend.run(p) && clawRotate.run(p) && rotate.run(p) && claw.run(p)
    }

    var targetPosition: IntakePositions = PICKUP
        set(value) =
            when (value) {
                PICKUP -> {
                    pendul.targetPosition = Positions.IntakePendul.pickupWait
                    rotate.targetPosition = Positions.IntakeRotate.pickup
                    extend.targetPosition = Positions.Extend.`out`
                }

                TRANSFER -> {
                    pendul.targetPosition = Positions.IntakePendul.transfer
                    rotate.targetPosition = Positions.IntakeRotate.transfer
                    extend.targetPosition = Positions.Extend.`in`

                    clawRotate.targetPosition = Positions.IntakeClawRotate.middle
                }
            }

    var isClosed
        get() = claw.isClosed
        set(value) {
            claw.isClosed = value
        }

    fun pickUp() {
        if (targetPosition != PICKUP) {
            return
        }

        needsPickup = true
        pickupTimer.resetTimer()
    }
}
