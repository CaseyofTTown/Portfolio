import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import jones.software.myPantryJournal.R
import jones.software.myPantryJournal.model.Recipe
import jones.software.myPantryJournal.sealedClasses.UiState

@Composable
fun HomeScreen(
    recipes: List<Recipe>,
    screenWidthDp: Int,
    onRecipeSelected: (Recipe) -> Unit,
    uiState: UiState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        /*
        Image(painterResource(R.drawable.your_background_image), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

         */
        Column {

            // Recipe count
            Text(
                text = stringResource(R.string.total_recipes, recipes.size),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = FontFamily.SansSerif
            )
            // Recipe collection
            MainRecipeCollection(
                recipes = recipes,
                screenWidthDp = screenWidthDp,
                //if the uiState is not editing, user wants to view recipe
                onRecipeSelected = { recipe ->
                    onRecipeSelected(recipe)
                },
                uiState = uiState
            )
        }
    }
}

@Composable
fun MainRecipeCollection(
    recipes: List<Recipe>?,
    screenWidthDp: Int,
    onRecipeSelected: (Recipe) -> Unit,
    uiState: UiState
) {
    Box(modifier = Modifier) {
        // Function to calculate number of columns based on screen size or orientation
        fun calculateColumns(): Int {
            // Decide the number of columns based on the screen width
            return when {
                screenWidthDp >= 600 -> 3 // Large devices
                screenWidthDp >= 360 -> 2 // Medium devices
                else -> 1 // Small devices
            }
        }
        if (recipes != null) {
            if (recipes.isNotEmpty()) {
                val columns =
                    calculateColumns() // Function to calculate number of columns based on screen size or orientation
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            modifier = Modifier.semantics {
                                contentDescription =
                                    "Recipe for ${recipe.recipeName}" // Adding content description for accessibility
                            },
                            onRecipeSelected = onRecipeSelected,
                            uiState = uiState
                        )
                    }
                }
            } else {
                //TODO Display a message or a composable when the list is empty
            }
        } else {
            // TODO Display a loading composable when recipes is null
        }
    }
}

//the card it is displaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import jones.software.myPantryJournal.R
import jones.software.myPantryJournal.model.Recipe
import jones.software.myPantryJournal.sealedClasses.UiState
import kotlin.math.min


@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeSelected: (Recipe) -> Unit,
    modifier: Modifier = Modifier,
    uiState: UiState,
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onRecipeSelected(recipe) },
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Card title
                Text(
                    text = recipe.recipeName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val imageModifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp))
                recipe.photoUrl?.let {
                    GlideImage(
                        imageModel = it,
                        contentDescription = "Image for ${recipe.recipeName}",
                        modifier = imageModifier,
                        error = { R.drawable.baseline_broken_image_24 }
                    )
                }
                Text(
                    text = stringResource(id = R.string.ingredients),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                for (i in 0 until min(3, recipe.ingredients.size)) {
                    val ingredient = recipe.ingredients[i]
                    Text(
                        text = "• ${ingredient.item} (${ingredient.amount.quantity} ${ingredient.amount.unit})",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        if (uiState == UiState.IsEditing) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = modifier.clickable { onRecipeSelected(recipe) })
            }
        }
    }
