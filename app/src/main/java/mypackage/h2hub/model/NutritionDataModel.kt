package mypackage.h2hub.model

data class NutritionDataModel(
    // Define your data model properties here
    //@SerializedName("id")
    //val id: Int,
    //@SerializedName("name")
    val name: String,
    // Add other properties as per your API response
    val calories: Double,
    val carbs: Double,
    val fat: Double,
    val protein: Double
)