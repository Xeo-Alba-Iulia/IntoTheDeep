package org.firstinspires.ftc.teamcode.nextftc

import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.ClawRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.Pendul
import org.firstinspires.ftc.teamcode.systems.OuttakePosition

object Outtake {
    var targetPosition = OuttakePosition.PICKUP
        private set

    val goTransfer get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goTransfer,
                Pendul.goTransfer,
            ),
            InstantCommand { targetPosition = OuttakePosition.TRANSFER },
        )

    val goBar get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goBar,
                Pendul.goBar,
            ),
            InstantCommand { targetPosition = OuttakePosition.BAR },
        )

    val goBasket get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goBasket,
                Pendul.goBasket,
            ),
            InstantCommand { targetPosition = OuttakePosition.BASKET },
        )

    val goPickup get() =
        SequentialGroup(
            ParallelGroup(
                ClawRotate.goPickup,
                Pendul.goPickup,
            ),
            InstantCommand { targetPosition = OuttakePosition.PICKUP },
        )
}
