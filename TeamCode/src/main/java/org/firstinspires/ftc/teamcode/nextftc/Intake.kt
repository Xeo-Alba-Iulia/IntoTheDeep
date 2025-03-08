package org.firstinspires.ftc.teamcode.nextftc

import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.*

object Intake : SubsystemGroup(Extend, IntakeClaw, IntakeClawRotate, IntakePendul) {
    val toTransfer get() =
        ParallelGroup(
            Extend.retract,
            IntakeClawRotate.goMiddle,
            IntakePendul.goTransfer,
        )

    val toPickupWait get() =
        ParallelGroup(
            Extend.extend,
            IntakePendul.goPickupWait,
        )

    val pickup get() =
        SequentialGroup(
            IntakePendul.goPickup,
            IntakeClaw.close,
            toPickupWait,
        )

    val isExtended
        get() = Extend.isExtended

    val switch get() = if (isExtended) toTransfer else toPickupWait
}
