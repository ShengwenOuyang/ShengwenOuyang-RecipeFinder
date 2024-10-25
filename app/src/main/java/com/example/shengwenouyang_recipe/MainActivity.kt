package com.example.shengwenouyang_recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shengwenouyang_recipe.network.Recipe
import com.example.shengwenouyang_recipe.ui.theme.ShengwenOuyangRecipeTheme
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShengwenOuyangRecipeTheme {
                RecipeApp()
            }
        }
    }
}

@Composable
fun RecipeApp(recipeViewModel: RecipeViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "recipeList") {
        composable("recipeList") {
            RecipeListScreen(navController, recipeViewModel)
        }
        composable("recipeDetails/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            recipeId?.let {
                RecipeDetailsScreen(recipeViewModel = recipeViewModel, recipeId = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(navController: NavController, recipeViewModel: RecipeViewModel) {
    val recipes by recipeViewModel.recipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Finder") }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                SearchBar(
                    onSearch = { query, cuisine ->
                        recipeViewModel.searchRecipes(query, cuisine)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                RecipeList(recipes = recipes) { recipeId ->
                    navController.navigate("recipeDetails/$recipeId")
                }
            }
        }
    )
}

@Composable
fun SearchBar(onSearch: (String, String?) -> Unit) {
    var query by remember { mutableStateOf("") }
    var cuisine by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter ingredients or recipe name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cuisine,
            onValueChange = { cuisine = it },
            label = { Text("Enter cuisine type (e.g., Italian)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onSearch(query, cuisine.ifEmpty { null })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe, onRecipeClick)
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onRecipeClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRecipeClick(recipe.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(recipe.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberImagePainter(data = recipe.image),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun RecipeDetailsScreen(recipeViewModel: RecipeViewModel, recipeId: Int) {
    val recipe by recipeViewModel.selectedRecipe.collectAsState()

    // Fetch the recipe details only if the current selected recipe is null or doesn't match the required ID
    LaunchedEffect(recipeId) {
        if (recipe == null || recipe?.id != recipeId) {
            recipeViewModel.getRecipeDetails(recipeId)
        }
    }

    recipe?.let {
        // Recipe is found, display its details
        Column(modifier = Modifier.padding(16.dp)) {
            Text(it.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberImagePainter(data = it.image),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
            it.ingredients?.forEach { ingredient ->
                Text("- ${ingredient.amount} ${ingredient.name}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Instructions:", style = MaterialTheme.typography.titleMedium)
            it.instructions?.let { instructions ->
                Text(instructions)
            }
        }
    } ?: run {
        // Display a loading state or message if the recipe is not yet available
        Text("Loading...", modifier = Modifier.padding(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShengwenOuyangRecipeTheme {
        RecipeApp()
    }
}
