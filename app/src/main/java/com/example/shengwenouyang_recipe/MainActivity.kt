package com.example.shengwenouyang_recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeApp(recipeViewModel: RecipeViewModel = viewModel()) {
    val recipes by recipeViewModel.recipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Finder") }
            )
        },
        content = { padding ->
            BoxWithConstraints(modifier = Modifier.padding(padding)) {
                if (maxWidth < 600.dp) {
                    Column {
                        SearchBar(
                            onSearch = { query, cuisine, diet, maxCalories ->
                                recipeViewModel.searchRecipes(query, cuisine, diet, maxCalories)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        RecipeList(recipes = recipes)
                    }
                } else {

                    Row {
                        Column(modifier = Modifier.weight(2f)) {
                            SearchBar(
                                onSearch = { query, cuisine, diet, maxCalories ->
                                    recipeViewModel.searchRecipes(query, cuisine, diet, maxCalories)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(2f)) {
                            RecipeList(recipes = recipes)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SearchBar(onSearch: (String, String?, String?, Int?) -> Unit) {
    var query by remember { mutableStateOf("") }
    var cuisine by remember { mutableStateOf("") }
    var diet by remember { mutableStateOf("") }
    var maxCalories by remember { mutableStateOf("") }

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

        TextField(
            value = diet,
            onValueChange = { diet = it },
            label = { Text("Enter diet type (e.g., vegetarian)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = maxCalories,
            onValueChange = { maxCalories = it },
            label = { Text("Enter max calories") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onSearch(query, cuisine.ifEmpty { null }, diet.ifEmpty { null }, maxCalories.toIntOrNull())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe)
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShengwenOuyangRecipeTheme {
        RecipeApp()
    }
}
