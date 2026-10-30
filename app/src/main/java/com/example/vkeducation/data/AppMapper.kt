package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppIcon
import com.example.vkeducation.domain.model.AppInfo

/** Converts the source format into the Android-independent domain model. */
class AppMapper {
    fun map(dto: AppDto): AppInfo = AppInfo(
        icon = when (dto.iconKey) {
            "telegram" -> AppIcon.TELEGRAM
            "video" -> AppIcon.VIDEO
            "gallery" -> AppIcon.GALLERY
            "music" -> AppIcon.MUSIC
            "maps" -> AppIcon.MAPS
            "notes" -> AppIcon.NOTES
            "books" -> AppIcon.BOOKS
            "weather" -> AppIcon.WEATHER
            else -> AppIcon.GENERIC
        },
        name = dto.name,
        description = dto.description,
        category = dto.category
    )
}
