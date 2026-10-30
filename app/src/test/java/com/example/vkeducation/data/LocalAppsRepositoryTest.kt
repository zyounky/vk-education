package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppIcon
import com.example.vkeducation.domain.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalAppsRepositoryTest {
    @Test
    fun usesInjectedSourceAndMapperInSourceOrderIncludingDuplicateNames() {
        val records = listOf(
            AppDto("first", "Same name", "First", "One"),
            AppDto("second", "Same name", "Second", "Two"),
            AppDto("third", "Other name", "Third", "Three")
        )
        var sourceCalls = 0
        val mappedRecords = mutableListOf<AppDto>()
        val expected = records.mapIndexed { index, dto ->
            AppInfo(AppIcon.GENERIC, "Mapped $index", dto.description, dto.category)
        }
        val repository = LocalAppsRepository(
            dataSource = {
                sourceCalls++
                records
            },
            mapper = { dto ->
                mappedRecords += dto
                expected[mappedRecords.lastIndex]
            }
        )

        val applications = repository.loadApps()

        assertEquals(1, sourceCalls)
        assertEquals(records, mappedRecords)
        assertEquals(expected, applications)
    }

    @Test
    fun readsCurrentSourceOnEachLoadAndAcceptsAnEmptyCatalog() {
        var records = listOf(AppDto("telegram", "Telegram", "Messenger", "Communication"))
        var sourceCalls = 0
        var mapperCalls = 0
        val realMapper = AppMapper()
        val repository = LocalAppsRepository(
            dataSource = {
                sourceCalls++
                records
            },
            mapper = {
                mapperCalls++
                realMapper.map(it)
            }
        )

        assertEquals(listOf("Telegram"), repository.loadApps().map { it.name })
        records = emptyList()
        assertEquals(emptyList<AppInfo>(), repository.loadApps())
        assertEquals(2, sourceCalls)
        assertEquals(1, mapperCalls)
    }

    @Test
    fun defaultRepositoryPreservesThePreviousCatalogAndOrderAfterMapping() {
        val expected = listOf(
            AppInfo(AppIcon.TELEGRAM, "Telegram", "Мессенджер для общения", "Общение"),
            AppInfo(AppIcon.VIDEO, "YouTube", "Платформа для просмотра видео", "Развлечения"),
            AppInfo(AppIcon.GALLERY, "Галерея", "Просмотр фотографий", "Фото"),
            AppInfo(AppIcon.MUSIC, "Музыка", "Любимые треки и плейлисты для любого настроения", "Музыка и аудио"),
            AppInfo(AppIcon.MAPS, "Карты", "Места рядом и удобные маршруты по городу", "Навигация"),
            AppInfo(AppIcon.NOTES, "Заметки", "Идеи, списки дел и всё, что важно не забыть", "Продуктивность"),
            AppInfo(AppIcon.BOOKS, "Книги", "Ваша библиотека всегда под рукой", "Книги и справочники"),
            AppInfo(AppIcon.WEATHER, "Погода", "Прогноз на сегодня и планы на неделю", "Погода")
        )

        assertEquals(expected, LocalAppsRepository().loadApps())
    }
}
