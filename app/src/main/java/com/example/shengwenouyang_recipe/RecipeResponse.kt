package com.example.shengwenouyang_recipe.network

import com.squareup.moshi.Json

data class RecipeResponse(
    @Json(name = "results")
    val recipes: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String
)
