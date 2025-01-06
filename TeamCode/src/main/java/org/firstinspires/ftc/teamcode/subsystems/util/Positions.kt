package org.firstinspires.ftc.teamcode.subsystems.util

object Positions {
    object IntakeRotation {
        const val parallel = 0.345
        const val perpendicular = 0.6466
    }

    object Pendul {
        const val transfer = 0.93
        const val outtake = 0.2
        const val smash = 0.0
        const val basket = 0.49
    }

    object Lift {
        const val down = 0.0
        const val half = 70.0
        const val up = 110.0
    }

    object Extend {
        const val `in` = 0.32
        const val out = 0.6
    }

    object IntakePendul {
        const val down = 0.43
        const val entrance = 0.50
        const val up = 0.84
    }

    object Claw {
        val open: Double = 0.4
        val close: Double = 0.95
    }

    object ClawRotate {
        val transfer: Double = 0.9
        val outtake: Double = 0.4
        val basket: Double = 0.3
    }
}