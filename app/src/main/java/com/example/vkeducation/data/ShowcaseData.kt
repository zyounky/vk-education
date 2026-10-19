package com.example.vkeducation.data

import com.example.vkeducation.R
import com.example.vkeducation.model.AppInfo

// Exactly nine fixed applications: three pages, three items per page.
val showcaseApps = listOf(
    AppInfo(R.drawable.ic_app_telegram, "Telegram", "Мессенджер для общения", "Общение"),
    AppInfo(R.drawable.ic_app_video, "YouTube", "Платформа для просмотра видео", "Развлечения"),
    AppInfo(R.drawable.ic_app_gallery, "Галерея", "Просмотр фотографий", "Фото"),
    AppInfo(R.drawable.ic_app_music, "Музыка", "Любимые треки и плейлисты", "Музыка и аудио"),
    AppInfo(R.drawable.ic_app_maps, "Карты", "Места рядом и маршруты по городу", "Навигация"),
    AppInfo(R.drawable.ic_app_notes, "Заметки", "Идеи и списки дел", "Продуктивность"),
    AppInfo(R.drawable.ic_app_books, "Книги", "Ваша библиотека всегда под рукой", "Книги и справочники"),
    AppInfo(R.drawable.ic_app_weather, "Погода", "Прогноз на сегодня и на неделю", "Погода"),
    AppInfo(R.drawable.guild_heroes_icon, "Гильдия Героев", "Приключения в фэнтези-мире", "Игры"),
)

const val SHOWCASE_PAGE_COUNT = 3
const val SHOWCASE_APPS_PER_PAGE = 3

val showcasePages = showcaseApps.chunked(SHOWCASE_APPS_PER_PAGE)
