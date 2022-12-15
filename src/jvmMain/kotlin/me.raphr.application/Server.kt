package me.raphr.application

import TodoListItem
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Liste contenant toutes les tâches.
 */
val todoList = mutableListOf(
    TodoListItem("Une tâche importante  à faire","Très important !!!",9999),
    TodoListItem("Une tâche pas importante à faire","Pas très important ...",-9999),
)

/**
 * Fonction principale du programme définissant les paramêtres du serveur et les routes valides.
 */
fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module).start(wait = true)
}

fun Application.module() {
    plugins()
    routing()
}

/**
 * Module d'installation des plugins requis
 */
fun Application.plugins() {
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        anyHost()
    }
    install(Compression) {
        gzip()
    }
}

/**
 * Module définissant les routes de l'application
 */
fun Application.routing() {
    routing {
        // Route principale contenant la liste des tâches
        get ("/") {
            call.respondText(
                this::class.java.classLoader.getResource("index.html")!!.readText(),
                ContentType.Text.Html
            )
        }
        // Emplacement des fichiers statiques
        static("/static") {
            resources("")
        }
        // Routes d'API pour la manipulation des données
        route(TodoListItem.path) {
            get {
                // Retourner toutes les tâches
                call.respond(todoList)
            }
            post {
                // Ajouter une tâche
                todoList += call.receive<TodoListItem>()
                call.respond(HttpStatusCode.OK)
            }
            delete("/{id}") {
                // Supprimer une tâche
                val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                todoList.removeIf { it.id == id }
                call.respond(HttpStatusCode.OK)
            }
            patch("{id}") {
                // Inverser le status de complétion de la tâche
                val id = call.parameters["id"]?.toInt() ?: error("Invalid update request")

                val item = todoList.first{ it.id == id }
                todoList.removeIf { it.id == id }

                item.done = !item.done
                todoList.add(item)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}