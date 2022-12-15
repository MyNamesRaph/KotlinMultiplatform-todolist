import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 *
 */
class TodoListItemTest {
    @Test
    fun testTodoListItem_Key_Is_Unique() {
        val title = "Title"
        val desc = "Description"
        val priority = 1
        val item1 = TodoListItem(title,desc,priority)
        val item2 = TodoListItem(title,desc,priority)

        assertFalse {item1.id == item2.id}
    }

    @Test
    fun testTodoListItem_Not_Done_By_Default() {
        val title = "Title"
        val desc = "Description"
        val priority = 1
        val item = TodoListItem(title,desc,priority)

        assertFalse {item.done}
    }

    @Test
    fun testTodoListItem_Serializes() {
        val title = "Title"
        val desc = "Description"
        val priority = 1
        val item = TodoListItem(title,desc,priority)

        val jsonStr = Json.encodeToString(item)

        assertEquals("""{"title":"$title","description":"$desc","priority":$priority,"id":0}""",jsonStr)
    }

    @Test
    fun testTodoListItem_Deserializes() {
        val title = "Title"
        val desc = "Description"
        val priority = 1
        val item = TodoListItem(title,desc,priority)


        val jsonStr = """{"title":"$title","description":"$desc","priority":$priority,"id":${item.id}}"""

        assertEquals(item.title,Json.decodeFromString<TodoListItem>(jsonStr).title)
    }
}