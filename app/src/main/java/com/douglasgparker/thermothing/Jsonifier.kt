package com.douglasgparker.thermothing

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.util.*
import kotlin.reflect.KProperty

fun JsonElement.asTimeOfDay() = gson.fromJson<TimeOfDay>(this)
fun JsonElement.asDayOfWeekSet() = gson.fromJson<EnumSet<DayOfWeek>>(this)

// Take a lambda provider and call it only once to get the required value and cache
// on all subsequent usages, return the same value. This is useful for caching unchanging values
// within a lambda expression without requiring declaration of a temp
fun cache(provider: () -> String) = object {
    var result: String? = null
    operator fun getValue(thisRef: Any?, property: KProperty<*>) : String {
        if (result == null) result = provider()
        return result!!
    }
}

// Initialize and configure JSON serializer
val gson: Gson by lazy {
    GsonBuilder().registerTypeAdapter<ThermoActivity.TempArgs> {
        serialize {
            jsonObject(
                "temp" to it.src.temp.toJson()
            )
        }

        deserialize { ThermoActivity.TempArgs(it.json["temp"].asFloat) }
    }.registerTypeAdapter<TimeOfDay> {
        serialize {
            jsonObject(
                "hour" to it.src.hour.toJson(),
                "minute" to it.src.minute.toJson()
            )
        }

        deserialize { TimeOfDay(it.json["hour"].asInt, it.json["minute"].asInt) }
    }.registerTypeAdapter<DayOfWeek> {
        serialize {
            it.src.day.toJson()
        }

        deserialize {
            DayOfWeek.values().find { dayOfWeek -> dayOfWeek.day.toLowerCase() == cache { it.json.asString.toLowerCase() } }
        }
    }.registerTypeAdapter<Event> {
        serialize {
            jsonObject(
                "temp" to it.src.temp.toJson(),
                "startTime" to it.src.startTime.toJson(),
                "endTime" to it.src.endTime.toJson(),
                "daysOfWeek" to it.src.daysOfWeek.toJsonArray()
            )
        }

        deserialize {
            Event(it.json["temp"].asFloat, it.json["startTime"].asTimeOfDay(),
                it.json["endTime"].asTimeOfDay(), it.json["daysOfWeek"].asDayOfWeekSet())
        }
    }.create()
}