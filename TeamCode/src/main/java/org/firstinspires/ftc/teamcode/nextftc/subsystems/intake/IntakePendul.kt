package org.firstinspires.ftc.teamcode.nextftc.subsystems.intake

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.units.ms
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToSeperatePositions
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakePendulPositions.Companion.pickup
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakePendulPositions.Companion.pickupWait
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakePendulPositions.Companion.search
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakePendulPositions.Companion.transfer

@Config
private class IntakePendulPositions private constructor() {
    companion object {
        @JvmField @Volatile
        var pickupWait = Pair(0.4, 0.1)

        @JvmField @Volatile
        var pickup = Pair(0.2, 0.05)

        @JvmField @Volatile
        var transfer = Pair(0.8, 0.75)

        @JvmField @Volatile
        var search = Pair(0.32, 0.08)
    }
}

object IntakePendul : Subsystem() {
    private lateinit var servo: Servo
    private lateinit var rotateServo: Servo

    override fun initialize() {
        servo = OpModeData.hardwareMap.servo["IntakePendul"]
        rotateServo = OpModeData.hardwareMap.servo["IntakeRotate"]
    }

    private fun goToPosition(
        position: Pair<Double, Double>,
        otherSystems: Set<Subsystem> = setOf(),
    ): Command =
        MultipleServosToSeperatePositions(
            mapOf(
                servo to position.first,
                rotateServo to position.second,
            ),
            otherSystems + this,
        )

    val goPickupWait get() = goToPosition(pickupWait)
    val goTransfer get() = goToPosition(transfer)
    val toSearch get() = goToPosition(search)

    val goPickup: Command get() =
        goToPosition(
            pickup,
            setOf(IntakeClawRotate, IntakeClaw, Extend),
        ).thenWait(200.ms)
}
