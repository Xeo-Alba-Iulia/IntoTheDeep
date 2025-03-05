package org.firstinspires.ftc.teamcode.nextftc

import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.ClawRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.outtake.Pendul

object Outtake {
    var inTransfer = false
        private set

    val goTransfer get() =
        {
            inTransfer = true
            ParallelGroup(
                ClawRotate.goTransfer,
                Pendul.goTransfer,
            )
        }()

    val goBar get() =
        {
            inTransfer = false
            ParallelGroup(
                ClawRotate.goBar,
                Pendul.goBar,
            )
        }()

    val goBasket get() =
        {
            inTransfer = false
            ParallelGroup(
                ClawRotate.goBasket,
                Pendul.goBasket,
            )
        }()

    val goPickup get() =
        {
            inTransfer = false
            ParallelGroup(
                ClawRotate.goPickup,
                Pendul.goPickup,
            )
        }()
}
