package me.dl33.wordplay

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(CIO) {
        routing {
            get("/") {
                call.respondText("hi")
            }
        }
    }.start(true)
}
