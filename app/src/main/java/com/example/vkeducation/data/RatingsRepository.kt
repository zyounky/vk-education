package com.example.vkeducation.data

import javax.inject.Inject
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

data class MeasuredRatings(val byAppId: Map<String, Float>, val elapsedMillis: Long)

class RatingsRepository @Inject constructor(
    private val api: SimulatedRatingsApi
) {
    suspend fun loadSequentially(appIds: List<String>): MeasuredRatings {
        val ratings: Map<String, Float>
        val elapsedMillis = measureTimeMillis {
            ratings = appIds.associateWith { id -> api.getRating(id) }
        }
        return MeasuredRatings(ratings, elapsedMillis)
    }

    suspend fun loadInParallel(appIds: List<String>): MeasuredRatings = coroutineScope {
        val ratings: Map<String, Float>
        val elapsedMillis = measureTimeMillis {
            ratings = appIds.map { id ->
                async { id to api.getRating(id) }
            }.awaitAll().toMap()
        }
        MeasuredRatings(ratings, elapsedMillis)
    }
}
