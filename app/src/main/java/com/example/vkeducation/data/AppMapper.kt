package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppInfo
import javax.inject.Inject

class AppMapper @Inject constructor() {
    fun map(dto: AppDto): AppInfo = AppInfo(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = dto.category,
        iconUrl = dto.iconUrl
    )
}
