package com.example.vkeducation.data

import com.example.vkeducation.domain.model.AppInfo
import com.example.vkeducation.domain.repository.AppsRepository

/** The source representation stays in Data; callers receive only domain models. */
class LocalAppsRepository(
    private val dataSource: () -> List<AppDto> = HardcodedAppsDataSource()::loadApps,
    private val mapper: (AppDto) -> AppInfo = AppMapper()::map
) : AppsRepository {
    override fun loadApps(): List<AppInfo> = dataSource().map(mapper)
}
