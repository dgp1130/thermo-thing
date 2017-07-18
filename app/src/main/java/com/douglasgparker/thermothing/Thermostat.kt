package com.douglasgparker.thermothing

// Object representing and controlling the attached thermostat
object Thermostat {
    // Actual temperature in celsius
    val actualTemp: Float
        inline get() = 38f

    // Desired temperature in celsius
    private var _desiredTemp = 23f
    var desiredTemp: Float
        get() = _desiredTemp
        set(value) {
            _desiredTemp = value
        }
}