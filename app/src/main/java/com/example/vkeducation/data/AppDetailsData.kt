package com.example.vkeducation.data

import com.example.vkeducation.R
import com.example.vkeducation.model.AppDetails

// The ready-made example card from maltsevmicyou/VkEducation, b4c673b.
val practiceAppDetails = AppDetails(
    name = "Гильдия Героев: Экшен ММО РПГ",
    developer = "VK Play",
    category = "Игры",
    ageRating = 12,
    size = 223.7f,
    iconRes = R.drawable.guild_heroes_icon,
    screenshotResList = listOf(
        R.drawable.guild_heroes_screenshot_1,
        R.drawable.guild_heroes_screenshot_2,
        R.drawable.guild_heroes_screenshot_3,
    ),
    description = "Легендарный рейд героев в Фэнтези РПГ. Станьте героем гильдии и сразите мастера подземелья!",
)
