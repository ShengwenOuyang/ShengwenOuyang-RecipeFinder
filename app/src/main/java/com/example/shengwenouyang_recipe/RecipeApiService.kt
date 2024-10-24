package com.example.shengwenouyang_recipe.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.spoonacular.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface RecipeApiService {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String? = null,
        @Query("cuisine") cuisine: String? = null,
        @Query("diet") diet: String? = null,
        @Query("intolerances") intolerances: String? = null,
        @Query("includeIngredients") includeIngredients: String? = null,
        @Query("excludeIngredients") excludeIngredients: String? = null,
        @Query("maxCalories") maxCalories: Int? = null
    ): RecipeResponse
}

object RecipeApi {
    val retrofitService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}
