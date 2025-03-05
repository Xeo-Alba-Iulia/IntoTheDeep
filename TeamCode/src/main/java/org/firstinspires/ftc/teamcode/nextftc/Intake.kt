package org.firstinspires.ftc.teamcode.nextftc

import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.NullCommand
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.Extend
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeClaw
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeClawRotate
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakePendul
import org.firstinspires.ftc.teamcode.nextftc.subsystems.intake.IntakeRotate

object Intake {
    var inTransfer = true
        private set

    val goTransfer get() =
        {
            inTransfer = true
            ParallelGroup(
                Extend.retract,
                IntakeClawRotate.goMiddle,
                IntakePendul.goTransfer,
                IntakeRotate.goTransfer,
            )
        }()

    val goPickupWait get() =
        {
            inTransfer = false
            ParallelGroup(
                Extend.extend,
                IntakePendul.goPickupWait,
                IntakeRotate.goPickupWait,
            )
        }()

    val pickup
        get() =
            if (inTransfer) {
                NullCommand()
            } else {
                SequentialGroup(
                    IntakeRotate.goPickup,
                    IntakePendul.goPickup,
                    IntakeClaw.close,
                    goPickupWait,
                )
            }
}
