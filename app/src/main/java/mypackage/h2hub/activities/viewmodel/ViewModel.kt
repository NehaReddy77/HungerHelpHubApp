package mypackage.h2hub.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import mypackage.h2hub.api.ApiService
import mypackage.h2hub.model.NutritionResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class FoodItemNutrition(
    val foodItemName: String,
    val nutritionResponse: NutritionResponse
)

class InfoViewModel : ViewModel() {

    private val apiKey = "574e8a6d354d459b8c01ebf8e21960bc"

    private val _apiData = MutableLiveData<List<FoodItemNutrition>>()
    val apiData: LiveData<List<FoodItemNutrition>> get() = _apiData

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun fetchData(foodItem: String) {
        viewModelScope.launch {
            try {
                val nutritionData = apiService.getNutritionData(apiKey, foodItem)
                val foodItemNutrition = FoodItemNutrition(foodItem, nutritionData)
                _apiData.value = listOf(foodItemNutrition)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchMultipleFoods(foodItems: List<String>) {
        viewModelScope.launch {
            try {
                val responses = foodItems.map { foodItem ->
                    val nutritionData = apiService.getNutritionData(apiKey, foodItem)
                    FoodItemNutrition(foodItem, nutritionData)
                }

                _apiData.value = responses
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
