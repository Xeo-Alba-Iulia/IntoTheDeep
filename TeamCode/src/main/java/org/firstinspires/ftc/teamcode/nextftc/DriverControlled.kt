package org.firstinspires.ftc.teamcode.nextftc

import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.hardware.Drivetrain
import com.rowanmcalpin.nextftc.pedro.FollowerNotInitializedException
import com.rowanmcalpin.nextftc.pedro.PedroData
import java.util.function.DoubleSupplier

class DriverControlled
    @JvmOverloads
    constructor(
        val driveSupplier: DoubleSupplier,
        val strafeSupplier: DoubleSupplier,
        val turnSupplier: DoubleSupplier,
        val robotCentric: Boolean = true,
        val invertDrive: Boolean = false,
        val invertStrafe: Boolean = false,
        val invertTurn: Boolean = false,
    ) : Command() {
        override val isDone: Boolean = false

        override val subsystems: Set<Drivetrain> = setOf(Drivetrain)

        override fun start() {
            if (PedroData.follower == null) {
                throw FollowerNotInitializedException()
            }
            PedroData.follower!!.startTeleopDrive()
        }

        private fun Boolean.invSign() = if (this) -1 else 1

        override fun update() {
            PedroData.follower!!.setTeleOpMovementVectors(
                driveSupplier.asDouble * if (invertDrive) -1 else 1,
                strafeSupplier.asDouble * if (invertStrafe) -1 else 1,
                turnSupplier.asDouble * if (invertTurn) -1 else 1,
                robotCentric,
            )
        }
    }
