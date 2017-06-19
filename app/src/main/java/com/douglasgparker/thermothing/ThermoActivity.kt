package com.douglasgparker.thermothing

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 * Main Activity class for the project.
 */
class ThermoActivity : Activity() {
    val TAG: String = "Thermo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Hello World!")
    }
}
