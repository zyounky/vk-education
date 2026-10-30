package com.example.vkeducation.data

/** Local stand-in for a future remote or database source. */
class HardcodedAppsDataSource {
    fun loadApps(): List<AppDto> = listOf(
        AppDto(
            iconKey = "telegram",
            name = "Telegram",
            description = "Мессенджер для общения",
            category = "Общение"
        ),
        AppDto(
            iconKey = "video",
            name = "YouTube",
            description = "Платформа для просмотра видео",
            category = "Развлечения"
        ),
        AppDto(
            iconKey = "gallery",
            name = "Галерея",
            description = "Просмотр фотографий",
            category = "Фото"
        ),
        AppDto(
            iconKey = "music",
            name = "Музыка",
            description = "Любимые треки и плейлисты для любого настроения",
            category = "Музыка и аудио"
        ),
        AppDto(
            iconKey = "maps",
            name = "Карты",
            description = "Места рядом и удобные маршруты по городу",
            category = "Навигация"
        ),
        AppDto(
            iconKey = "notes",
            name = "Заметки",
            description = "Идеи, списки дел и всё, что важно не забыть",
            category = "Продуктивность"
        ),
        AppDto(
            iconKey = "books",
            name = "Книги",
            description = "Ваша библиотека всегда под рукой",
            category = "Книги и справочники"
        ),
        AppDto(
            iconKey = "weather",
            name = "Погода",
            description = "Прогноз на сегодня и планы на неделю",
            category = "Погода"
        )
    )
}
