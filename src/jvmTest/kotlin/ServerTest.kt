import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.raphr.application.module
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ServerTest {
    @Test
    fun testServer_Root_Http_OK() = testApplication {
        application {
            module()
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testServer_Root_Is_Index() = testApplication {
        application {
            module()
        }
        val response = client.get("/")
        assertEquals(this::class.java.classLoader.getResource("index.html")!!.readText(), response.bodyAsText())
    }

    @Test
    fun testServer_Static_Serves_JS_Script() = testApplication {
        application {
            module()
        }
        val response = client.get("/static/TodoList.js")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testServer_Get_TodoList_Http_OK() = testApplication {
        application {
            module()
        }
        val response = client.get("/todo")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testServer_Post_TodoList_Http_OK() = testApplication {
        application {
            module()
        }
        val response = client.post("/todo") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(TodoListItem("Title","Description",0)))
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testServer_Post_TodoList_Adds_To_List() = testApplication {
        application {
            module()
        }

        val item = TodoListItem("Title","Description",0)

        client.post("todo") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(item))
        }

        val response = client.get("/todo")

        assertContains(
            response.bodyAsText(),
            """{"title":"${item.title}","description":"${item.description}","priority":${item.priority},"id":${item.id},"done":${item.done}"""
        )
    }

    @Test
    fun testServer_Delete_TodoList_Http_OK() = testApplication {
        application {
            module()
        }
        val item = TodoListItem("Title","Decription",0)

        val response = client.delete("/todo/${item.id}")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testServer_Delete_TodoList_Deletes_From_List() = testApplication {
        application {
            module()
        }

        val item = TodoListItem("Title","Decription",0)

        client.delete("/todo/${item.id}")

        val response = client.get("/todo")

        assertFails { assertContains(response.bodyAsText(),",id:1,") }
    }

    @Test
    fun testServer_Patch_TodoList_Http_OK() = testApplication {
        application {
            module()
        }
        // Le premier id est 0 et il y a des items dans la liste par défaut.
        val response = client.patch("/todo/0")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testServer_Patch_TodoList_Sets_Done() = testApplication {
        application {
            module()
        }
        // Le premier id est 0 et il y a des items dans la liste par défaut.
        client.patch("/todo/0")
        val response = client.get("/todo")

        assertContains(response.bodyAsText(),""""done":true""")
    }
}