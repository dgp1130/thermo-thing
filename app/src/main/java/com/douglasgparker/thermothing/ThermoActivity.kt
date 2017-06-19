package com.douglasgparker.thermothing

import android.app.Activity
import android.os.Bundle
import android.util.Log
import spark.Spark
import spark.Spark.*

/**
 * Main Activity class for the project.
 */
class ThermoActivity : Activity() {
    val TAG = "Thermo"
    val PORT = 8000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Initializing...")

        // Initialize server
        Spark.port(PORT)
        get("/hello") { _, _ -> "Hello World!" }

        Log.v(TAG, "Serving on port $PORT...")
    }
}
