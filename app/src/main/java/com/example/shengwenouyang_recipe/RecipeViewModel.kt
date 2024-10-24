package com.example.shengwenouyang_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shengwenouyang_recipe.network.Recipe
import com.example.shengwenouyang_recipe.network.RecipeApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    fun searchRecipes(query: String, cuisine: String? = null, diet: String? = null, maxCalories: Int? = null) {
        viewModelScope.launch {
            try {
                val response = RecipeApi.retrofitService.searchRecipes(
                    apiKey = "3dee876fd7194a33932ffc2ecb470355",
                    query = query,
                    cuisine = cuisine,
                    diet = diet,
                    maxCalories = maxCalories
                )
                _recipes.value = response.recipes
            } catch (e: Exception) {
                _recipes.value = emptyList()
            }
        }
    }
}
