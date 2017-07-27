package com.douglasgparker.thermothing

import com.google.android.things.contrib.driver.pwmservo.Servo

/**
 * Class representing a Motor on the pin given by pinName.
 *
 * Wraps a Servo object to provide a better API for a 360 degree motor. Normally, Servo.angle can be
 * set to the desired angle, but for 360 degree motors, doing so merely sets the speed and direction
 * that it is rotating in. We need to manually compute how long to wait for the motor to get to
 * where it needs to go and then disable the servo so it does not spin perpetually.
 */
class Motor(pinName: String) {
    val ANGLE_NEUTRAL = 0.0
    val ANGLE_CLOCKWISE = 135.0
    val ANGLE_COUNTER_CLOCKWISE = -135.0
    val ANGLE_MIN = -360.0
    val ANGLE_MAX = 360.0

    val ROTATION_TIME_MS = 650 // Takes 1100 ms to rotate 360 degrees, rounding down significantly to account for startup time
    val CIRCLE_DEG = 360.0 // 360 degrees in a circle

    val servo: Servo = Servo(pinName)

    init {
        servo.setAngleRange(ANGLE_MIN, ANGLE_MAX)
    }

    private var _angle = 0.0f
    var angle: Float
        get() = _angle
        set(value) {
            // Rotate to the new position
            if (value > _angle) {
                rotateClockwise(value - _angle)
            } else {
                rotateCounterClockwise(_angle - value)
            }

            // Update internal angle value with new position
            _angle = value
        }

    // Rotate the servo clockwise by the given number of degrees
    private fun rotateClockwise(deg: Float) {
        moveServo(servo) {
            servo.angle = ANGLE_CLOCKWISE
            Thread.sleep(degToRotationTime(deg))
            servo.angle = ANGLE_NEUTRAL
        }
    }

    // Rotate the servo counter clockwise by the given number of degrees
    private fun rotateCounterClockwise(deg: Float) {
        moveServo(servo) {
            servo.angle = ANGLE_COUNTER_CLOCKWISE
            Thread.sleep(degToRotationTime(deg))
            servo.angle = ANGLE_NEUTRAL
        }
    }

    // Convert a degree value to a time to wait for the servo to rotate that many degrees
    private fun degToRotationTime(deg: Float) : Long = (deg / CIRCLE_DEG * ROTATION_TIME_MS).toLong()

    // Helper function to enable and disable the servo while invoking the callback to be used to move the servo
    private fun moveServo(servo: Servo, callback: () -> Unit) {
        servo.setEnabled(true)
        callback.invoke()
        servo.setEnabled(false)
    }
}