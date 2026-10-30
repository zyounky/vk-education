package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppIcon
import com.example.vkeducation.domain.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class AppMapperTest {
    private val mapper = AppMapper()

    @Test
    fun mapsEveryCatalogIconToItsDomainIdentifier() {
        val expectedIcons = linkedMapOf(
            "telegram" to AppIcon.TELEGRAM,
            "video" to AppIcon.VIDEO,
            "gallery" to AppIcon.GALLERY,
            "music" to AppIcon.MUSIC,
            "maps" to AppIcon.MAPS,
            "notes" to AppIcon.NOTES,
            "books" to AppIcon.BOOKS,
            "weather" to AppIcon.WEATHER
        )

        expectedIcons.forEach { (key, expectedIcon) ->
            val dto = AppDto(key, "Application", "Description", "Category")
            assertEquals("Unexpected icon for $key", expectedIcon, mapper.map(dto).icon)
        }
    }

    @Test
    fun preservesDisplayFieldsWithoutTrimmingOrReplacingThem() {
        val dto = AppDto("books", " Книги ", "Описание: café 📚", "")

        assertEquals(
            AppInfo(AppIcon.BOOKS, " Книги ", "Описание: café 📚", ""),
            mapper.map(dto)
        )
    }

    @Test
    fun unknownOrEmptyIconUsesGenericWithoutLosingTheApplication() {
        listOf("future-icon", "", "TELEGRAM").forEach { unknownKey ->
            val dto = AppDto(unknownKey, "New app", "Available soon", "Utilities")

            assertEquals(
                AppInfo(AppIcon.GENERIC, dto.name, dto.description, dto.category),
                mapper.map(dto)
            )
        }
    }
}
