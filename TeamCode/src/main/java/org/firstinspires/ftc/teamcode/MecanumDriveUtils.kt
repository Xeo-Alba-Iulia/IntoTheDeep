package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder

/**
 * Create an [Action] from a [TrajectoryActionBuilder] block, using kotlin DSL
 */
fun MecanumDrive.trajectoryAction(
    beginPose: Pose2d,
    block: TrajectoryActionBuilder.() -> Unit,
): Action = actionBuilder(beginPose).apply(block).build()
