package com.example.vkeducation.domain.model

data class AppDetails(
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Double,
    val iconUrl: String?,
    val screenshots: List<String>,
    val description: String
)
