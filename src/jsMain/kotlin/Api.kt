import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

/**
 * Va chercher la liste de tâche depuis l'API
 */
suspend fun getTodoList(): List<TodoListItem> {
    return jsonClient.get(TodoListItem.path).body()
}

/**
 * Ajoute une tâche à la liste depuis l'API
 */
suspend fun addTodoListItem(todoListItem: TodoListItem) {
    jsonClient.post(TodoListItem.path) {
        contentType(ContentType.Application.Json)
        setBody(todoListItem)
    }
}

/**
 * Supprime une tâche de la liste depuis l'API
 */
suspend fun deleteTodoListItem(todoListItem: TodoListItem) {
    jsonClient.delete(TodoListItem.path + "/${todoListItem.id}")
}

/**
 * Inverse le statut de complétion de la tâche depuis l'API
 */
suspend fun setTodoListItem(todoListItem: TodoListItem) {
    jsonClient.patch(TodoListItem.path + "/${todoListItem.id}")
}