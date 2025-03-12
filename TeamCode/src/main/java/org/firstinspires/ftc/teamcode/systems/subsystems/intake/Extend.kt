package org.firstinspires.ftc.teamcode.systems.subsystems.intake

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import org.firstinspires.ftc.teamcode.systems.subsystems.util.ServoPositionMechanism
import org.firstinspires.ftc.teamcode.util.ServoSmooth
import kotlin.math.abs

class Extend(
    hardwareMap: HardwareMap,
) : ServoPositionMechanism(Positions.Extend.`in`) {
    override val servos = arrayOf(hardwareMap.servo["Extend1"], hardwareMap.servo["Extend2"])

    val servoSmooth = ServoSmooth(0.97)

    override var targetPosition: Double = Positions.Extend.`in`
        get() {
            if (abs(servoSmooth.targetPosition - field) < 0.02) {
                return servoSmooth.targetPosition
            }

            val result = servoSmooth[field]
            field = result
            return result
        }
        set(value) {
            servoSmooth.targetPosition = value.coerceIn(Positions.Extend.`in`..Positions.Extend.out)
        }
}
