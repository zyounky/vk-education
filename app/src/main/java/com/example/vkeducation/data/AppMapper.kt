package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppInfo

class AppMapper {
    fun map(dto: AppDto): AppInfo = AppInfo(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        category = dto.category,
        iconUrl = dto.iconUrl
    )
}
