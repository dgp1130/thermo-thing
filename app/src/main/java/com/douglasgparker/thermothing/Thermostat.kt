package com.douglasgparker.thermothing

import android.util.Log

// Object representing and controlling the attached thermostat
object Thermostat {
    val TAG = "Thermostat"

    val SERVO_PIN_NAME = "PWM0"

    val MIN_TEMP = 4.4 // C = 40 F
    val MAX_TEMP = 32.2 // C = 90 F
    val RANGE_TEMP = MAX_TEMP - MIN_TEMP
    val MIN_ANGLE = -90.0 // West on the thermostat
    val MAX_ANGLE = 90.0 // East on the thermostat
    val RANGE_ANGLE = MAX_ANGLE - MIN_ANGLE

    // Actual temperature in celsius
    val actualTemp: Float
        inline get() = 38f

    // Desired temperature in celsius
    private var _desiredTemp = 18.3f // Neutral position of thermostat
    var desiredTemp: Float
        get() = _desiredTemp
        set(value) {
            _desiredTemp = value

            // Move motor to new location
            val angle = tempToAngle(value)
            Log.v(TAG, "Rotating to angle: $angle")
            motor.angle = angle
        }

    private val motor = Motor(SERVO_PIN_NAME)

    // Convert a temperature value (Celsius) to an angle for the motor to rotate to
    private fun tempToAngle(temp: Float) = ((((temp - MIN_TEMP) / RANGE_TEMP) * RANGE_ANGLE) + MIN_ANGLE).toFloat()
}