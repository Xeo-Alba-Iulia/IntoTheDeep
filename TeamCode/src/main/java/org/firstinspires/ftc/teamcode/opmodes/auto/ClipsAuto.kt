package org.firstinspires.ftc.teamcode.opmodes.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.pathgen.PathBuilder
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotHardware
import org.firstinspires.ftc.teamcode.systems.IntakePositions
import org.firstinspires.ftc.teamcode.systems.OuttakePosition
import org.firstinspires.ftc.teamcode.systems.subsystems.util.Positions
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

@Autonomous
class ClipsAuto : LinearOpMode() {
    private val pathTimer = Timer()
    private var state = 0
        set(value) {
            pathTimer.resetTimer()
            field = value
        }

    override fun runOpMode() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        val follower = Follower(hardwareMap)
        val hubList = hardwareMap.getAll(LynxModule::class.java)

        val robot = RobotHardware(hardwareMap)

        val dashboard = FtcDashboard.getInstance()
        val angle = Math.PI
        val beginPoint = Point(9.0, 60.5)
        val scorePoint =
            arrayOf(
                Point(40.0, 67.5),
                Point(40.0, 68.0),
                Point(40.0, 68.5),
                Point(40.0, 69.0),
                Point(40.0, 69.5),
            )
        val samplePoint =
            arrayOf(
                Point(57.7, 27.0),
                Point(57.7, 17.0),
                Point(57.7, 8.9),
            )
        val dropPoint =
            arrayOf(
                Point(28.0, samplePoint[0].y),
                Point(28.0, samplePoint[1].y),
                Point(17.0, samplePoint[2].y),
            )

        val scoreControl = Point(20.0, scorePoint[1].y)
        val pickupPoint = Point(17.0, 30.0)

        val scorePathList =
            mutableListOf(
                PathBuilder()
                    .addBezierLine(beginPoint, scorePoint[0])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) {
                        robot.lift.targetPosition = Positions.Lift.half
                        robot.outtake.outtakePosition = OuttakePosition.BAR
                    }.build(),
                PathBuilder()
                    .addBezierCurve(dropPoint[2], scoreControl, scorePoint[1])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) { robot.lift.targetPosition = Positions.Lift.half }
                    .addTemporalCallback(0.6) { robot.outtake.outtakePosition = OuttakePosition.BAR }
                    .build(),
            )

        scorePathList +=
            Array(3) {
                PathBuilder()
                    .addBezierLine(pickupPoint, scorePoint[it + 2])
                    .setConstantHeadingInterpolation(angle)
                    .addTemporalCallback(0.0) { robot.lift.targetPosition = Positions.Lift.half }
                    .addTemporalCallback(0.6) { robot.outtake.outtakePosition = OuttakePosition.BAR }
                    .build()
            }

        val scorePath = scorePathList.toTypedArray()

        robot.outtake.pendul.targetPosition = 0.95
        robot.intake.targetPosition = IntakePositions.TRANSFER

        while (opModeInInit()) {
            val p = TelemetryPacket()
            robot.intake.run(p)
            robot.outtake.pendul.run(p)
            dashboard.sendTelemetryPacket(p)
        }

        while (isStopRequested) {
        }
    }
}
