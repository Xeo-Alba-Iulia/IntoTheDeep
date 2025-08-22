package org.firstinspires.ftc.teamcode.systems.subsystems.outtake

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ManualMechanismTeleOp
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.util.ServoSmooth
import javax.inject.Inject
import kotlin.math.abs

class Pendul
    @Inject
    constructor(
        hardwareMap: HardwareMap,
    ) : ServoPositionMechanism(0.8) {
        override val servos: Array<Servo> =
            arrayOf(hardwareMap.servo["Pendul1"], hardwareMap.servo["Pendul2"])

        var useServoSmooth = true

        val servoSmooth = ServoSmooth(0.96)

        override var targetPosition: Double = 0.8
            get() {
                if (abs(servoSmooth.targetPosition - field) < 0.01) {
                    return servoSmooth.targetPosition
                }

                val result = servoSmooth[field]
                field = result
                return result
            }
            set(value) {
                servoSmooth.targetPosition = value
                if (!useServoSmooth) {
                    field = value
                }
            }
    }

@TeleOp(name = "Outtake pendul test", group = "C")
class PendulTest : ManualMechanismTeleOp(::Pendul)
