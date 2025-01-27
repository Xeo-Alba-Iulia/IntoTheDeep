package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.config.Config

@Config
object Positions {
    object IntakeRotation {
        @JvmField
        @Volatile
        var parallel = 0.345

        @JvmField
        @Volatile
        var perpendicular = 0.6466
    }

    object Pendul {
        @JvmField
        @Volatile
        var transfer = 0.93

        @JvmField
        @Volatile
        var outtake = 0.2

        @JvmField
        @Volatile
        var smash = 0.0

        @JvmField
        @Volatile
        var basket = 0.49
    }

    object Lift {
        @JvmField
        @Volatile
        var down = 0.0

        @JvmField
        @Volatile
        var half = 70.0

        @JvmField
        @Volatile
        var up = 110.0
    }

    object Extend {
        @JvmField
        @Volatile
        var `in` = 0.32

        @JvmField
        @Volatile
        var out = 0.6
    }

    object IntakePendul {
        @JvmField
        @Volatile
        var down = 0.43

        @JvmField
        @Volatile
        var entrance = 0.50

        @JvmField
        @Volatile
        var up = 0.84
    }

    object Claw {
        @JvmField
        @Volatile
        var open: Double = 0.4

        @JvmField
        @Volatile
        var close: Double = 1.0
    }

    object ClawRotate {
        @JvmField
        @Volatile
        var transfer: Double = 0.9

        @JvmField
        @Volatile
        var outtake: Double = 0.4

        @JvmField
        @Volatile
        var basket: Double = 0.3
    }
}
