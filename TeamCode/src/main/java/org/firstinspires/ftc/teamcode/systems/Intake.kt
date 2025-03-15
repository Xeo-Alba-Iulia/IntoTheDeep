package org.firstinspires.ftc.teamcode.systems

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.IntakePositions.*
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

    val isPickedUp get() = pickupTimer.elapsedTimeSeconds >= 0.24

    private lateinit var oldPosition: IntakePositions

    override fun run(p: TelemetryPacket): Boolean {
        if (needsPickup) {
            oldPosition = targetPosition
            pendul.targetPosition = Positions.IntakePendul.pickup
            rotate.targetPosition = Positions.IntakeRotate.pickup
        }

        if (needsPickup && pickupTimer.elapsedTimeSeconds >= 0.12) {
            isClosed = true
        }

        if (needsPickup && pickupTimer.elapsedTimeSeconds >= 0.23) {
            if (targetPosition == oldPosition) {
                targetPosition = oldPosition
            }
            needsPickup = false
        }

        return pendul.run(p) && extend.run(p) && clawRotate.run(p) && rotate.run(p) && claw.run(p)
    }

    var targetPosition: IntakePositions = PICKUP
        set(value) {
            field = value
            when (value) {
                PICKUP -> {
                    pendul.targetPosition = Positions.IntakePendul.pickupWait
                    rotate.targetPosition = Positions.IntakeRotate.pickupWait
                    extend.targetPosition = Positions.Extend.`out`
                }

                TRANSFER -> {
                    pendul.targetPosition = Positions.IntakePendul.transfer
                    rotate.targetPosition = Positions.IntakeRotate.transfer
                    extend.targetPosition = Positions.Extend.`in`

                    clawRotate.targetPosition = Positions.IntakeClawRotate.middle
                }

                SEARCH -> {
                    pendul.targetPosition = Positions.IntakePendul.search
                    rotate.targetPosition = Positions.IntakeRotate.search
                    extend.targetPosition = Positions.Extend.`out`

                    clawRotate.targetPosition = Positions.IntakeClawRotate.middle
                }

                SPECIMEN_PICKUP -> {
                    pendul.targetPosition = Positions.IntakePendul.wallPickup
                    rotate.targetPosition = Positions.IntakeRotate.wallPickup
                    extend.targetPosition = Positions.Extend.`in`

                    clawRotate.targetPosition = Positions.IntakeClawRotate.middle
                }
            }
        }

    var isClosed
        get() = claw.isClosed
        set(value) {
            claw.isClosed = value
        }

    companion object {
        val pickupPositions = arrayOf(PICKUP, SEARCH)
    }

    fun pickUp() {
        if (isExtended) {
            pickupTimer.resetTimer()
            needsPickup = true
        }
    }

    fun switch() {
        targetPosition = if (isExtended) TRANSFER else PICKUP
    }

    val isExtended get() = targetPosition in pickupPositions
}
