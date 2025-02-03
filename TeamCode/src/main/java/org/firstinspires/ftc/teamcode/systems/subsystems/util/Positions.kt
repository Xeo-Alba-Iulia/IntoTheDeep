package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.config.Config

object Positions {
    @Config
    class IntakeRotation {
        companion object {
            @JvmField
            @Volatile
            var parallel = 0.345

            @JvmField
            @Volatile
            var perpendicular = 0.6466
        }
    }

    @Config
    class Pendul {
        companion object {
            @JvmField
            @Volatile
            var transfer = 0.0

            @JvmField
            @Volatile
            var bar = 0.71

            @JvmField
            @Volatile
            var basket = 0.0

            @JvmField
            @Volatile
            var pickup = 0.87
        }
    }

    @Config
    class Lift {
        companion object {
            @JvmField
            @Volatile
            var down = 0.0

            @JvmField
            @Volatile
            var half = 450.0

            @JvmField
            @Volatile
            var up = 2150.0
        }
    }

    @Config
    class Extend {
        companion object {
            @JvmField
            @Volatile
            var `in` = 0.32

            @JvmField
            @Volatile
            var out = 0.6
        }
    }

    @Config
    class IntakePendul {
        companion object {
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
    }

    @Config
    class Claw {
        companion object {
            @JvmField
            @Volatile
            var open: Double = 0.469

            @JvmField
            @Volatile
            var close: Double = 0.621
        }
    }

    @Config
    class ClawRotate {
        companion object {
            @JvmField
            @Volatile
            var transfer: Double = 0.9

            @JvmField
            @Volatile
            var bar: Double = 0.2

            @JvmField
            @Volatile
            var basket: Double = 0.0

            @JvmField
            @Volatile
            var pickup = 0.08
        }
    }
}
