package com.example.vkeducation.domain.repository

import com.example.vkeducation.domain.model.AppInfo

fun interface AppsRepository {
    fun loadApps(): List<AppInfo>
}
