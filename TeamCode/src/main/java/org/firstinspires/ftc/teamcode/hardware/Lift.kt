package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.util.PIDController

class Lift(hardwareMap: HardwareMap): Action {
    enum class LiftState(val position: Int) {
        UP(0),
        HALF(0),
        DOWN(0)
    }

    companion object {
        val devicesRequired = listOf(
            "MotorLiftLeft",
            "MotorLiftRight"
        )
    }

    val liftLeft: DcMotorEx
    val liftRight: DcMotorEx

    val lift: Array<DcMotorEx>

    val controller = PIDController(0.01, 0.0, 0.0)
    protected open var target = 0

    /**
     * Target position for all pendul motors
     *
     * @see DcMotorEx.getTargetPosition
     * @see DcMotorEx.setTargetPosition
     */
    var targetPosition = LiftState.DOWN
        set(value) {
            field = value
            target = value.position
        }


    init {
        val deviceIterator = devicesRequired.iterator()

        liftLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        liftRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())

        lift = arrayOf(liftLeft, liftRight)

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        setMode(DcMotor.RunMode.RUN_USING_ENCODER)

        liftRight.direction = DcMotorSimple.Direction.REVERSE
    }

    private fun setMode(mode: DcMotor.RunMode) {
        lift.forEach { it.mode = mode }
    }

    private fun setPower(power: Double) {
        lift.forEach { it.power = power }
    }

    private fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        lift.forEach { it.zeroPowerBehavior = behavior }
    }

    fun update(){
        val power = controller.update(target.toDouble(), liftRight.currentPosition.toDouble())
        setPower(power)
    }

    override fun run(p: TelemetryPacket): Boolean {
        TODO("Not yet implemented")
        p.put("Lift Left Position", liftLeft.currentPosition)
        p.put("Lift Right Position", liftRight.currentPosition)
    }
}



