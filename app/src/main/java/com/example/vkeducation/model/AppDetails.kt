package com.example.vkeducation.model

/** The practical lesson's card model, with packaged images for offline use. */
data class AppDetails(
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Float,
    val iconRes: Int,
    val screenshotResList: List<Int>,
    val description: String,
)
