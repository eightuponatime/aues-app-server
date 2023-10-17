package com.example.plugins

import com.example.Connection
import com.example.parseScheduleAsync
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.LinkedHashSet


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }

        post("/addUser") {
            try {
                val student = call.receive<Student>()
                insertDatabase(student)
                call.respond(true)
            } catch (e: Exception) {
                call.respond(false)
            }
        }

        post("/login") {
            try {
                val user = call.receive<User>()
                val isAuthenticated = logInDatabase(user)
                if(isAuthenticated) {
                    call.respond(true)
                } else {
                    call.respond(false)
                }
            } catch (e: Exception) {
                call.respond(false)
            }
        }

        post("/parse-schedule") {
            try {
                val user = call.receive<User>()
                val schedule = runBlocking(Dispatchers.IO) {
                    parseScheduleAsync(user)
                }
                call.respond(schedule)
            } catch (e: Exception) {
                call.respond("")
            }
        }

        post("/getPassword") {
            try {
                val username = call.receive<Username>()
                val password = selectPassword(username)
                call.respond(password)
            } catch (e : Exception) {
                call.respond("")
            }
        }

        post("/getGroup") {
            try {
                val username = call.receive<Username>()
                val group = selectGroup(username)
                call.respond(group)
            } catch(e: Exception) {
                call.respond("")
            }
        }

        post("/getFirstName") {
            try {
                val username = call.receive<Username>()
                val firstName = selectFirstName(username)
                call.respond(firstName)
            } catch (e: Exception) {
                call.respond("")
            }
        }

        post("/getLastName") {
            try {
                val username = call.receive<Username>()
                val lastName = selectLastName(username)
                call.respond(lastName)
            } catch (e: Exception) {
                call.respond("")
            }
        }

        post("/checkIfUserExists") {
            try {
                val username = call.receive<Username>()
                val existance = checkIfUserExists(username)
                if(existance) {
                    call.respond(true)
                } else {
                    call.respond(false)
                }
            } catch (e: Exception) {
                call.respond(false)
            }
        }

        post("/setImage") {
            try {
                val setImg = call.receive<SetImage>()
                setImage(setImg)
                call.respond(true)
            } catch (e: Exception) {
                call.respond(false)
            }
        }

        post("/getImage") {
            try {
                val getImg = call.receive<GetImage>()
                val imageBytes = getImage(getImg)
                if (imageBytes != null) {
                    call.respondBytes(imageBytes, ContentType.Image.JPEG)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/getAllUsersFromDatabase") {
            call.respond(getAllUsersFromDatabase())
        }
    }
}
