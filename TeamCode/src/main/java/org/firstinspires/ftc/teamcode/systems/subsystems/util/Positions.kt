package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.config.Config

object Positions {
    @Config
    class Pendul {
        companion object {
            @JvmField
            @Volatile
            var transfer = 0.19

            @JvmField
            @Volatile
            var bar = 0.71

            @JvmField
            @Volatile
            var basket = 0.58

            @JvmField
            @Volatile
            var pickup = 0.834
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
            var transfer = 100.0

            @JvmField
            @Volatile
            var half = 650.0

            @JvmField
            @Volatile var hang = 1700.0

            @JvmField
            @Volatile
            var up = 3200.0
        }
    }

    @Config
    class Extend {
        companion object {
            @JvmField
            @Volatile
            var `in` = 0.635

            @JvmField
            @Volatile
            var out = 0.9
        }
    }

    @Config
    class IntakePendul {
        companion object {
            @JvmField
            @Volatile
            var pickupWait = 0.33

            @JvmField @Volatile
            var pickup = 0.21

            @JvmField
            @Volatile
            var transfer = 0.8
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
            var transfer: Double = 0.83

            @JvmField
            @Volatile
            var bar: Double = 0.73

            @JvmField
            @Volatile
            var basket: Double = 0.27

            @JvmField
            @Volatile
            var pickup = 0.61
        }
    }

    @Config
    class IntakeClaw {
        companion object {
            @JvmField @Volatile
            var closed = 0.79

            @JvmField @Volatile
            var open = 0.46
        }
    }

    @Config
    class IntakeClawRotate {
        companion object {
            @JvmField @Volatile
            var middle = 0.525

            var right = 0.785

            var left = 0.265
        }
    }

    @Config
    class IntakeRotate {
        companion object {
            @JvmField @Volatile
            var pickupWait = 0.1

            @JvmField @Volatile
            var pickup = 0.05

            @JvmField @Volatile
            var transfer = 0.75
        }
    }
}
