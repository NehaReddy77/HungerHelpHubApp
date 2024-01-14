package mypackage.h2hub.api

import mypackage.h2hub.model.NutritionDataModel
import mypackage.h2hub.model.NutritionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("your_endpoint")
    fun getData(): List<NutritionDataModel>

    @GET("recipes/guessNutrition")
    suspend fun getNutritionData(@Query("apiKey") apiKey: String, @Query("title") title: String): NutritionResponse
}



