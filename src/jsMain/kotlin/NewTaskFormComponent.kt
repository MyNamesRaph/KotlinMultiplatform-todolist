import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.useState

external interface InputProps : Props {
    var onSubmit: (String,String,String) -> Unit
}

/**
 * Composant pour la création d'une nouvelle tâche
 */
val newTaskFormComponent = FC<InputProps> { props ->
    val (title, setTitle) = useState("")
    val (desc, setDesc) = useState("")
    val (priority, setPriority) = useState("")

    /**
     * Réinitialise les valeurs du formulaire
     */
    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setTitle("")
        setDesc("")
        setPriority("")
        props.onSubmit(title,desc,priority)
    }

    /**
     * Met à jour la valeur du titre lorsque l'utilisateur écrit dans l'entrée de texte
     */
    val titleChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setTitle(it.target.value)
    }

    /**
     * Met à jour la valeur de la description lorsque l'utilisateur écrit dans l'entrée de texte
     */
    val descChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setDesc(it.target.value)
    }

    /**
     * Met à jour la valeur de la priorité l'utilisateur écrit dans l'entrée de texte
     */
    val priorityChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setPriority(it.target.value)
    }

    // Définir le formulaire de création de tâche
    form {
        onSubmit = submitHandler

        // Entrée de titre
        label {
            htmlFor = "title"
            + "Titre : "
        }
        input {
            type = InputType.text
            onChange = titleChangeHandler
            value = title
            name = "title"
        }

        // Entrée de description
        label {
            htmlFor = "desc"
            + "Description : "
        }
        input {
            type = InputType.text
            onChange = descChangeHandler
            value = desc
            name = "desc"
        }

        // Entrée de priorité
        label {
            htmlFor = "priority"
            + "Priorité : "
        }
        input {
            type = InputType.number
            onChange = priorityChangeHandler
            value = priority
            name = "priority"
        }

        // Bouton d'envoi du formulaire
        button {
            + "Créer tâche"
        }
    }
}