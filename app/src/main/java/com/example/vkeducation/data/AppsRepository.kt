package com.example.vkeducation.data

import com.example.vkeducation.model.AppInfo

fun interface AppsRepository {
    fun loadApps(): List<AppInfo>
}

object LocalAppsRepository : AppsRepository {
    override fun loadApps(): List<AppInfo> = apps
}
