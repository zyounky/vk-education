package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppDetails
import javax.inject.Inject

class AppDetailsRepository @Inject constructor(private val api: AppsApi) {
    suspend fun loadAppDetails(id: String): AppDetails {
        val dto = api.getAppDetails(id)
        return AppDetails(
            name = dto.name,
            developer = dto.developer,
            category = dto.category,
            ageRating = dto.ageRating,
            size = dto.size,
            iconUrl = dto.iconUrl,
            screenshots = dto.screenshotUrlList,
            description = dto.description
        )
    }
}
