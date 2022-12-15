import TodoListItem.Companion.path
import kotlinx.serialization.Serializable


/**
 * Un item représentant une tâche dans une liste de tâches.
 *
 * @param title Le titre de la tâche.
 * @param description La description de la tâche.
 * @param priority La priorité de la tâche sur la liste.
 * @property id Clé d'identification unique de la tâche.
 * @property done État de complétion de la tâche.
 * @property path Chemin d'API où la liste de tâche peut être récupéré.
 */
@Serializable
class TodoListItem (val title: String, val description: String, val priority: Int) {
    val id: Int = incrementId
    var done: Boolean = false

    companion object {
        const val path = "/todo"
        var incrementId = -1
    }

    init {
        incrementId ++
    }
}