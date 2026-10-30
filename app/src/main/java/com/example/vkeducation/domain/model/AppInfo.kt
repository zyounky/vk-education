package com.example.vkeducation.domain.model

enum class AppIcon {
    TELEGRAM,
    VIDEO,
    GALLERY,
    MUSIC,
    MAPS,
    NOTES,
    BOOKS,
    WEATHER,
    GENERIC
}

data class AppInfo(
    val icon: AppIcon,
    val name: String,
    val description: String,
    val category: String
)
