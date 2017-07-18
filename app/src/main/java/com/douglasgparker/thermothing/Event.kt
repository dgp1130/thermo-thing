package com.douglasgparker.thermothing

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.toJsonArray
import java.io.File
import java.util.*

enum class DayOfWeek(val day: String) {
    Sunday("sunday"), Monday("Monday"), Tuesday("Tuesday"), Wednesday("Wednesday"),
    Thursday("Thursday"), Friday("Friday"), Saturday("Saturday")
}

data class Event(val temp: Float, val startTime: TimeOfDay, val endTime: TimeOfDay, val daysOfWeek: EnumSet<DayOfWeek>) {
    fun toJson() = gson.toJson(this)

    companion object {
        fun fromJson(str: String) = gson.fromJson<Event>(str)
    }
}

data class TimeOfDay(val hour: Int, val minute: Int) {
    // Validate inputs
    init {
        if (hour < 0 || hour > 23) throw IllegalArgumentException("Cannot create TimeOfDay with hour: $hour")
        if (minute < 0 || minute > 59) throw IllegalArgumentException("Cannot create TimeOfDay with minute: $minute")
    }

    fun toJson() = gson.toJson(this)

    companion object {
        fun fromJson(str: String) = gson.fromJson<TimeOfDay>(str)
    }
}

val eventsFile = File("events.json")
fun loadEvents(): List<Event> = eventsFile.bufferedReader().use {
    gson.fromJson<List<Event>>(it.readText())
}

fun saveEvents(events: List<Event>) = eventsFile.bufferedWriter().use {
    it.write(events.toJsonArray().toString())
}