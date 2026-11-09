package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppInfo
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteAppsRepositoryTest {
    private val requests = ConcurrentLinkedQueue<Request>()

    @Test
    fun readsCurrentCatalogAndPreservesIdsDisplayFieldsAndSourceOrder() = runTest {
        val api = createApi(
            """
            [
                {
                    "id": "first",
                    "name": " Заметки ",
                    "description": "Описание: café",
                    "category": "Работа",
                    "iconUrl": "https://example.com/notes.png"
                },
                {
                    "id": "second",
                    "name": " Заметки ",
                    "description": "Другие заметки",
                    "category": ""
                }
            ]
            """.trimIndent(),
            "[]"
        )
        val repository = RemoteAppsRepository(api, AppMapper())

        assertEquals(
            listOf(
                AppInfo(
                    id = "first",
                    name = " Заметки ",
                    description = "Описание: café",
                    category = "Работа",
                    iconUrl = "https://example.com/notes.png"
                ),
                AppInfo(
                    id = "second",
                    name = " Заметки ",
                    description = "Другие заметки",
                    category = ""
                )
            ),
            repository.loadApps()
        )
        assertEquals(emptyList<AppInfo>(), repository.loadApps())
        assertEquals(listOf("GET", "GET"), requests.map { it.method })
        assertEquals(listOf("/catalog", "/catalog"), requests.map { it.url.encodedPath })
    }

    @Test
    fun requestsAppDetailsByIdAndDeserializesTheResponse() = runTest {
        val appId = "fa2e31b8-1234-4cf7-9914-108a170a1b01"
        val api = createApi(
            """
            {
                "id": "$appId",
                "name": "QuickNote",
                "developer": "QuickSoft Inc.",
                "category": "Производительность",
                "ageRating": 3,
                "size": 8.2,
                "iconUrl": "https://example.com/icon.png",
                "screenshotUrlList": ["https://example.com/screen.png"],
                "description": "Подробное описание"
            }
            """.trimIndent()
        )

        assertEquals(
            AppDetailsDto(
                id = appId,
                name = "QuickNote",
                developer = "QuickSoft Inc.",
                category = "Производительность",
                ageRating = 3,
                size = 8.2,
                iconUrl = "https://example.com/icon.png",
                screenshotUrlList = listOf("https://example.com/screen.png"),
                description = "Подробное описание"
            ),
            api.getAppDetails(appId)
        )
        val request = requests.single()
        assertEquals("GET", request.method)
        assertEquals("/catalog/$appId", request.url.encodedPath)
    }

    private fun createApi(vararg responses: String): AppsApi {
        val bodies = ConcurrentLinkedQueue(responses.toList())
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                requests.add(request)
                val body = bodies.poll() ?: throw IOException("No response queued for ${request.url}")
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body(body.toResponseBody("application/json".toMediaType()))
                    .build()
            }
            .build()
        return Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppsApi::class.java)
    }
}
