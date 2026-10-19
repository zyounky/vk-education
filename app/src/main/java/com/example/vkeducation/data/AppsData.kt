package com.example.vkeducation.data

import com.example.vkeducation.R
import com.example.vkeducation.model.AppInfo

val apps = listOf(
    AppInfo(
        iconRes = R.drawable.ic_app_telegram,
        name = "Telegram",
        description = "Мессенджер для общения",
        category = "Общение"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_video,
        name = "YouTube",
        description = "Платформа для просмотра видео",
        category = "Развлечения"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_gallery,
        name = "Галерея",
        description = "Просмотр фотографий",
        category = "Фото"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_music,
        name = "Музыка",
        description = "Любимые треки и плейлисты для любого настроения",
        category = "Музыка и аудио"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_maps,
        name = "Карты",
        description = "Места рядом и удобные маршруты по городу",
        category = "Навигация"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_notes,
        name = "Заметки",
        description = "Идеи, списки дел и всё, что важно не забыть",
        category = "Продуктивность"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_books,
        name = "Книги",
        description = "Ваша библиотека всегда под рукой",
        category = "Книги и справочники"
    ),
    AppInfo(
        iconRes = R.drawable.ic_app_weather,
        name = "Погода",
        description = "Прогноз на сегодня и планы на неделю",
        category = "Погода"
    )
)
