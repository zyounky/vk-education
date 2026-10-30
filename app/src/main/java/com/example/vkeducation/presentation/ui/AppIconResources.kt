package com.example.vkeducation.presentation.ui

import androidx.annotation.DrawableRes
import com.example.vkeducation.R
import com.example.vkeducation.domain.model.AppIcon

@DrawableRes
internal fun AppIcon.toDrawableRes(): Int = when (this) {
    AppIcon.TELEGRAM -> R.drawable.ic_app_telegram
    AppIcon.VIDEO -> R.drawable.ic_app_video
    AppIcon.GALLERY -> R.drawable.ic_app_gallery
    AppIcon.MUSIC -> R.drawable.ic_app_music
    AppIcon.MAPS -> R.drawable.ic_app_maps
    AppIcon.NOTES -> R.drawable.ic_app_notes
    AppIcon.BOOKS -> R.drawable.ic_app_books
    AppIcon.WEATHER -> R.drawable.ic_app_weather
    AppIcon.GENERIC -> R.drawable.ic_catalog_app
}
