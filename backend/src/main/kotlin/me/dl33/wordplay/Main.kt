package me.dl33.wordplay

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.routing
import io.ktor.server.websocket.*
import io.ktor.websocket.*

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
sealed class UserMessage {
}
data class UserMove(val userSuffix: String) : UserMessage()

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
sealed class ServerMessage {

}
data class GameMoveResult(val moveResult: MoveResult) : ServerMessage()

val mapper = jacksonObjectMapper()

val game = Game(1, null, 5, StubDictionary(), "start")

fun main() {
    embeddedServer(CIO) {
        install(WebSockets) {
        }
        routing {
            // for now playing with yourself
            webSocket("/test") {
                for (frame in incoming) {
                    val rawMessage = (frame as? Frame.Text)?.readText() ?: continue
                    val m: UserMessage = mapper.readValue(rawMessage)
                    when (m) {
                        is UserMove -> {
                            val moveResult = game.tryMove(0, m.userSuffix)
                            send(mapper.writeValueAsString(GameMoveResult(moveResult)))
                        }
                    }
                }
            }
        }
    }.start(true)
}
