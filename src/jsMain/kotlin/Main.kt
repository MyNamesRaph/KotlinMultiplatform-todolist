import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

/**
 * Ajoute l'application à la racine du site
 */
fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(container).render(App.create())
}