package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.config.Config

object Positions {
    @Config
    class Pendul {
        companion object {
            @JvmField
            @Volatile
            var transfer = 0.03

            @JvmField
            @Volatile
            var bar = 0.71

            @JvmField
            @Volatile
            var basket = 0.64

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
            var up = 2210.0
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
            var pickup = 0.2195

            @JvmField
            @Volatile
            var init = 0.50

            @JvmField
            @Volatile
            var transfer = 0.84
        }
    }

    @Config
    class Claw {
        companion object {
            @JvmField
            @Volatile
            var open: Double = 0.5

            @JvmField
            @Volatile
            var close: Double = 0.654
        }
    }

    @Config
    class ClawRotate {
        companion object {
            @JvmField
            @Volatile
            var transfer: Double = 0.355

            @JvmField
            @Volatile
            var bar: Double = 0.8

            @JvmField
            @Volatile
            var basket: Double = 0.6

            @JvmField
            @Volatile
            var pickup = 0.68
        }
    }
}
