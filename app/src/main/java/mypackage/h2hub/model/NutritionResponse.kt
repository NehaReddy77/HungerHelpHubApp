package mypackage.h2hub.model

data class NutritionResponse(
    val calories: NutritionItem,
    val carbs: NutritionItem,
    val fat: NutritionItem,
    val protein: NutritionItem
)

data class NutritionItem(
    val value: Double,
    val unit: String
)
