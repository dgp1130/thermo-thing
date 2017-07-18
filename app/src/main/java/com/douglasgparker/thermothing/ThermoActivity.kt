package com.douglasgparker.thermothing

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.github.salomonbrys.kotson.*
import spark.Request

/**
 * Main Activity class for the project.
 */
class ThermoActivity : Activity() {
    val TAG = "Thermo"
    val PORT = 8000

    val SET_TEMP_ERROR = """Please specify the temperature in the body. This should look like:
{
    "temp": 72.0
}"""

    enum class HTTP(val code: Int) {
        OK(200),
        BAD_REQ(400)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Initializing...")

        // Initialize server
        Server.serve(PORT) {
            // Get the actual temperature and return it
            get("/actual-temp") { _, _ -> Thermostat.actualTemp }

            // Get the currently desired temperature
            get("/desired-temp") { _, _ -> Thermostat.desiredTemp }

            // Change the currently desired temperature to the given value
            post("/desired-temp") { req, res ->
                val args: TempArgs

                try {
                    args = req.json<TempArgs>()
                } catch (ex: Exception) {
                    Log.e(TAG, "Failed to set desired temperature with body:\n${req.body()}")
                    res.status(HTTP.BAD_REQ.code)
                    return@post SET_TEMP_ERROR
                }

                Thermostat.desiredTemp = args.temp
                return@post ""
            }
        }

        Log.v(TAG, "Serving on port $PORT...")
    }

    // Extract the body of the request parsed as JSON
    data class TempArgs(val temp: Float)
    inline fun <reified T : Any> Request.json() = gson.fromJson<T>(body() ?: "")
}
