package org.firstinspires.ftc.teamcode.systems.subsystems.util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.util.Constants
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

abstract class ManualMechanismTeleOp(
    private val factory: ManualPositionFactory,
) : OpMode() {
    protected lateinit var manualPositionMechanism: ManualPositionMechanism
    val dashboard = FtcDashboard.getInstance() as FtcDashboard
    lateinit var follower: Follower

    override fun init() {
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        follower = Follower(hardwareMap)
        follower.setStartingPose(Pose(0.0, 0.0, 0.0))

        manualPositionMechanism = factory.manualPositionFactory(hardwareMap)
    }

    override fun start() {
        follower.startTeleopDrive()
    }

    override fun loop() {
        follower.setTeleOpMovementVectors(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble(),
        )
        follower.update()

        manualPositionMechanism.targetPosition +=
            manualPositionMechanism.adjustMultiplier * (gamepad1.right_trigger - gamepad1.left_trigger)

        val packet = TelemetryPacket()

        if (manualPositionMechanism.run(packet) == false) {
            requestOpModeStop()
        }

        dashboard.sendTelemetryPacket(packet)
        dashboard.telemetry.update()

        telemetry.addData(
            "Position for ${manualPositionMechanism::class.simpleName}",
            manualPositionMechanism.targetPosition,
        )

        telemetry.update()
    }
}
