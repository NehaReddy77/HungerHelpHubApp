//package mypackage.h2hub.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import mypackage.h2hub.R
//import mypackage.h2hub.model.NutritionDataModel
//import mypackage.h2hub.model.NutritionResponse
//
//class ApiDataAdapter(private var apiDataList: List<NutritionDataModel>) :
//    RecyclerView.Adapter<ApiDataAdapter.ApiDataViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiDataViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.activity_nutrition_eachitem, parent, false)
//        return ApiDataViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ApiDataViewHolder, position: Int) {
//        val currentItem = apiDataList[position]
//
//        holder.textViewId.text = "ID: ${currentItem.id}"
//        holder.textViewName.text = "Name: ${currentItem.name}"
//
//        // Set other TextViews based on your data structure
//    }
//
//    override fun getItemCount(): Int = apiDataList.size
//
//    fun setData(newData: List<NutritionResponse>) {
//        apiDataList = newData
//        notifyDataSetChanged()
//    }
//
//    class ApiDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textViewId: TextView = itemView.findViewById(R.id.textViewId)
//        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
//
//        // Add other TextViews here
//    }
//}

package mypackage.h2hub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mypackage.h2hub.R
import mypackage.h2hub.activities.viewmodel.FoodItemNutrition

class ApiDataAdapter(private var apiDataList: List<FoodItemNutrition>) :
    RecyclerView.Adapter<ApiDataAdapter.ApiDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiDataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_nutrition_eachitem, parent, false)
        return ApiDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ApiDataViewHolder, position: Int) {
        val currentItem = apiDataList[position]

        holder.textViewName.text = "Food Item: ${currentItem.foodItemName}"
        holder.textViewCalories.text = "Calories: ${currentItem.nutritionResponse.calories.value} ${currentItem.nutritionResponse.calories.unit}"
        holder.textViewCarbs.text = "Carbs: ${currentItem.nutritionResponse.carbs.value} ${currentItem.nutritionResponse.carbs.unit}"
        holder.textViewFat.text = "Fat: ${currentItem.nutritionResponse.fat.value} ${currentItem.nutritionResponse.fat.unit}"
        holder.textViewProtein.text = "Protein: ${currentItem.nutritionResponse.protein.value} ${currentItem.nutritionResponse.protein.unit}"

        // Set other TextViews based on your NutritionResponse data structure
    }

    override fun getItemCount(): Int = apiDataList.size

    fun setData(newData: List<FoodItemNutrition>) {
        apiDataList = newData
        notifyDataSetChanged()
    }

    class ApiDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewCalories: TextView = itemView.findViewById(R.id.textViewCalories)
        val textViewCarbs: TextView = itemView.findViewById(R.id.textViewCarbs)
        val textViewFat: TextView = itemView.findViewById(R.id.textViewFat)
        val textViewProtein: TextView = itemView.findViewById(R.id.textViewProtein)

        // Add other TextViews here
    }
}

