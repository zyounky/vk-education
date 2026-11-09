package com.example.vkeducation.data

data class AppDetailsDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Double,
    val iconUrl: String?,
    val screenshotUrlList: List<String>,
    val description: String
)
