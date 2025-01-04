package org.firstinspires.ftc.teamcode.subsystems.util

object Positions {
    object IntakeRotation {
        const val parallel = 0.345
        const val perpendicular = 0.6466
    }

    object Pendul {
        const val transfer = 0.98
        const val outtake = 0.0
    }

    object Lift {
        const val down = 0.0
        const val half = 70.0
        const val up = 110.0
    }

    object Extend {
        const val `in` = 0.08
        const val out = 0.6
    }

    object IntakePendul {
        const val down = 0.31
        const val up = 0.84
    }

    object Claw {
        val open: Double = 0.0
        val close: Double = TODO("Find positions for claw")
    }

    object ClawRotate {
        val transfer: Double = 0.9
        val outtake: Double = 0.5
    }
}