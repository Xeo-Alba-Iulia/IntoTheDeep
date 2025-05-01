package org.firstinspires.ftc.teamcode.opmodes.audio

/**
 * This annotation is used to mark functions that are voice operations.
 *
 * The contract for this annotation is that all of its parameters must be serializable.
 *
 * A websocket api will be created from all the functions annotated with this annotation,
 * taking a JSON dictionary, with the keys being the function argument names.
 *
 * @author Vlad1-1
 * @see VoiceAssistantOpMode
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class VoiceActivated
