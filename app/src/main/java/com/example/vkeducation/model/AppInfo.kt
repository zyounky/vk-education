package com.example.vkeducation.model

import androidx.annotation.DrawableRes

data class AppInfo(
    @param:DrawableRes val iconRes: Int,
    val name: String,
    val description: String,
    val category: String
)
