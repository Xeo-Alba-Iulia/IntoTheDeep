package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition

abstract class DelayedOpMode : LinearOpMode() {
    abstract val delayedActions : MutableList<Pair<Long, Runnable>>;

    protected fun MutableList<Pair<Long, Runnable>>.addDelayed(
        delay: Double,
        runnable: Runnable,
    ) = add(
        Pair(
            System.currentTimeMillis() + (delay * 1000).toLong(),
            runnable,
        ),
    )

    protected fun MutableList<Pair<Long, Runnable>>.run() {
        val iterator = iterator()

        while (iterator.hasNext()) {
            val action = iterator.next()

            if (System.currentTimeMillis() >= action.first) {
                action.second.run()
                iterator.remove()
            }
        }
    }

    protected fun transfer(robot: RobotHardware) {
        val finishTransfer : () -> Unit = {
            if (robot.claw.isClosed) {
                robot.intake.claw.isClosed = true
                delayedActions.addDelayed(0.25) { robot.claw.isClosed = false }
            } else {
                robot.claw.isClosed = true
                delayedActions.addDelayed(0.25) { robot.intake.isClosed = false }
            }
        }

        robot.claw.isClosed = false
        val setOuttakeLiftPosition = {
            robot.outtake.outtakePosition = OuttakePosition.TRANSFER
            robot.lift.targetPosition = Positions.Lift.transfer
        }
        if (robot.intake.targetPosition != IntakePositions.TRANSFER) {
            robot.intake.targetPosition = IntakePositions.TRANSFER
            delayedActions.addDelayed(0.2, setOuttakeLiftPosition)
        } else {
            setOuttakeLiftPosition()
        }
        delayedActions.addDelayed(0.5, finishTransfer)
    }
}
