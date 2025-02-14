package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.config.Config

object Positions {
    @Config
    class Pendul {
        companion object {
            @JvmField
            @Volatile
            var transfer = 0.208

            @JvmField
            @Volatile
            var bar = 0.7

            @JvmField
            @Volatile
            var basket = 0.58

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
            var half = 481.0

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
            var pickupWait = 0.222

            @JvmField @Volatile
            var pickup = 0.1

            @JvmField
            @Volatile
            @Deprecated("Nu mai e pozitie de init")
            var init = 0.5

            @JvmField
            @Volatile
            var transfer = 0.88
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
            var transfer: Double = 0.87

            @JvmField
            @Volatile
            var bar: Double = 0.8

            @JvmField
            @Volatile
            var basket: Double = 0.27

            @JvmField
            @Volatile
            var pickup = 0.68
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
        }
    }

    @Config
    class IntakeRotate {
        companion object {
            @JvmField @Volatile
            var pickup = 0.017

            @JvmField @Volatile
            var transfer = 0.753
        }
    }
}
