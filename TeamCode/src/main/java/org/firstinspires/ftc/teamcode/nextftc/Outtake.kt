package org.firstinspires.ftc.teamcode.nextftc

import com.rowanmcalpin.nextftc.core.SubsystemGroup
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.*
import org.firstinspires.ftc.teamcode.systems.OuttakePosition

object Outtake : SubsystemGroup(Claw, ClawRotate, Pendul, OuttakeRotate) {
    var targetPosition = OuttakePosition.PICKUP
        private set

    val goTransfer get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goTransfer,
                Pendul.goTransfer,
                OuttakeRotate.toDown,
            ),
            InstantCommand { targetPosition = OuttakePosition.TRANSFER },
        )

    val goBar get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goBar,
                Pendul.goBar,
                OuttakeRotate.toUp,
            ),
            InstantCommand { targetPosition = OuttakePosition.BAR },
        )

    val goBasket get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goBasket,
                Pendul.goBasket,
                OuttakeRotate.toUp,
            ),
            InstantCommand { targetPosition = OuttakePosition.BASKET },
        )

    val goPickup get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goPickup,
                Pendul.goPickup,
                OuttakeRotate.toDown,
            ),
            InstantCommand { targetPosition = OuttakePosition.PICKUP },
        )

    val close get() = Claw.close
    val open get() = Claw.open
    val toggle get() = Claw.toggle

    val isClosed get() = Claw.isClosed
}
