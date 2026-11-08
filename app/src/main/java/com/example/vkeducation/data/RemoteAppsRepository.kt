package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository

class RemoteAppsRepository(
    private val api: AppsApi
) : AppsRepository {
    private val mapper = AppMapper()

    override suspend fun loadApps(): List<AppInfo> = api.getCatalog().map(mapper::map)
}
