import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.s
import react.dom.html.ReactHTML.strong
import react.dom.html.ReactHTML.ul

private val scope = MainScope()

/**
 * Composant principale de l'application définissant la disposition des éléments
 */
val App = FC<Props> {
    var todoList by useState(emptyList<TodoListItem>())

    useEffectOnce {
        // Aller chercher la valeur par défaut de la liste au lancement de la page
        scope.launch {
            todoList = getTodoList()
        }
    }

    h1 {
        + "Liste de tâches full-stack"
    }


    // Afficher le formulaire d'ajout de tâche
    h3 {
        +"Nouvelle tâche : "
    }
    newTaskFormComponent {
        onSubmit = { title: String, desc: String, priority: String ->
            val todo = TodoListItem(title,desc, priority.toInt())
            scope.launch {
                addTodoListItem(todo)
                todoList = getTodoList()
            }
        }
    }

    // Afficher la liste de tâches
    ul {
        todoList.sortedByDescending(TodoListItem::priority).forEach { item ->

            li  {
                key = item.id.toString()

                onClick = {
                    scope.launch {
                        setTodoListItem(item)
                        todoList = getTodoList()
                    }
                }

                if (item.done) {
                    s {
                        + "[${item.priority}] ${item.title}: item.description"
                    }
                }
                else {
                    strong {
                        + "[${item.priority}] ${item.title}: "
                    }
                    + item.description
                }
            }

            button {
                onClick = {
                    scope.launch {
                        deleteTodoListItem(item)
                        todoList = getTodoList()
                    }
                }
                + "\uD83D\uDDD1"
            }
        }
    }
}