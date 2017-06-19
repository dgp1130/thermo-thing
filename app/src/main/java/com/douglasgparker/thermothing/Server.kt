package com.douglasgparker.thermothing

import spark.Request
import spark.Response
import spark.Spark

// Wrapper object to provide a simpler interface for the Spark server
object Server {
    // Serve on the given port using the callback to specify the API contract
    fun serve(port: Int, cb: ServeMethods.() -> Unit) {
        Spark.port(port)
        cb(ServeMethods)
    }
}

// Make alias for Route with Kotlin types
typealias Route = (Request, Response) -> Any

// Helper object to allow easy creation of an API contract via extension methods
object ServeMethods {
    fun get(path: String, route: Route) {
        Spark.get(path, route)
    }

    fun post(path: String, route: Route) {
        Spark.post(path, route)
    }

    fun put(path: String, route: Route) {
        Spark.put(path, route)
    }

    fun delete(path: String, route: Route) {
        Spark.delete(path, route)
    }
}