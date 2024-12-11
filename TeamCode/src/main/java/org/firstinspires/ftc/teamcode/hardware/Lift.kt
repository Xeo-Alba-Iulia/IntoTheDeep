package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap

class Lift(hardwareMap: HardwareMap): Action {
    enum class LiftState {
        UP,
        DOWN,
        STOP
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

    init {
        val deviceIterator = devicesRequired.iterator()

        liftLeft = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())
        liftRight = hardwareMap.get(DcMotorEx::class.java, deviceIterator.next())

        lift = arrayOf(liftLeft, liftRight)

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

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

    fun lift(gamepad: Gamepad) {
        val y: Double = gamepad.right_stick_y.toDouble()

        liftLeft.power = y
        liftRight.power = y
    }

    fun moveToPosition(position: Int, power: Double ){

        liftLeft.targetPosition = position
        liftRight.targetPosition = position

        setMode(DcMotor.RunMode.RUN_TO_POSITION)
        setPower(power)

        while(liftLeft.isBusy && liftRight.isBusy){
            setPower(0.0)
        }
        fun lift(gamepad: Gamepad){
            val y: Double = gamepad.right_stick_y.toDouble()

            liftLeft.power = y
            liftRight.power = y
        }
    }

    override fun run(p: TelemetryPacket): Boolean {
        TODO("Not yet implemented")
        p.put("Lift Left Position", liftLeft.currentPosition)
        p.put("Lift Right Position", liftRight.currentPosition)
    }
}



