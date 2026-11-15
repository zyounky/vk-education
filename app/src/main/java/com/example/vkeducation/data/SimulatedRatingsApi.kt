package com.example.vkeducation.data

import javax.inject.Inject
import kotlinx.coroutines.delay

class SimulatedRatingsApi @Inject constructor() {
    suspend fun getRating(id: String): Float {
        delay(1_500)
        return (30 + Math.floorMod(id.hashCode(), 21)) / 10f
    }
}
