package com.douglasgparker.thermothing

// Object representing and controlling the attached thermostat
object Thermostat {
    val actualTemp: Float
        inline get() = 100f

    private var _desiredTemp = 72f
    var desiredTemp: Float
        get() = _desiredTemp
        set(value) {
            _desiredTemp = value
        }
}