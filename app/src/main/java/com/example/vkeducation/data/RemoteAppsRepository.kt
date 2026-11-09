package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository
import javax.inject.Inject

class RemoteAppsRepository @Inject constructor(
    private val api: AppsApi,
    private val mapper: AppMapper
) : AppsRepository {
    override suspend fun loadApps(): List<AppInfo> = api.getCatalog().map(mapper::map)
}
