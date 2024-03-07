import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource

@Composable
fun PanningImage(
    imageResource: Int,
    modifier: Modifier = Modifier,
    durationMillis: Int = 5000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "panning image")
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f, // Adjust this value for the desired panning distance
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "panning image"
    )

    // Animate the alpha value for fade in/out effect
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis / 2, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "fade in/out"
    )

    Image(
        painter = painterResource(id = imageResource),
        contentDescription = "Panning image",
        modifier = modifier
            .graphicsLayer {
                translationX = translateAnim
                alpha = alphaAnim
            }
    )
}


//the code in the viewmodel which calls this
 //loads funny messages from repository then posts to Ui until ui state changes
    fun displayLoadingDialogWithMessages() {
        Log.d(TAG, "Displaying loading Dialog and posting funny messages to flow val")
        //triggers the dialog
        _funnyLoadingMessage.value = "Asking wizard for funny messages"
        viewModelScope.launch {
            delay(500) // Add this line to delay the display of the dialog
            if (uiState.value == UiState.InProgress) {
                while (uiState.value == UiState.InProgress) {
                    _funnyLoadingMessage.value = repository.getRandomMessage()
                    imageCycler.getNextImage()
                    delay(3000)
                }
            }
            if (uiState.value != UiState.InProgress) {
                //set _shouldShowLoading... to false
            }
        }
    }

    //some of the messages that will be cycled
    <string name="preheating_the_app">Preheating the app…</string>
    <string name="chopping_pixels_into_place">Chopping pixels into place…</string>
    <string name="stirring_in_a_dash_of_fun">Stirring in a dash of fun…</string>
    <string name="seasoning_the_interface">Seasoning the interface…</string>
    <string name="baking_the_perfect_user_experience">Baking the perfect user experience…</string>
    <string name="fetching_secret_sauce_from_the_cloud">Fetching secret sauce from the cloud…</string>
    <string name="grilling_the_bugs_away">Grilling the bugs away…</string>
    <string name="whisking_up_some_fresh_content">Whisking up some fresh content…</string>
    <string name="saut_ing_firestore_database">Sautéing Firestore database…</string>
    <string name="rolling_out_the_dough_of_data">Rolling out the dough of data…</string>
    <string name="simmering_on_low_code_heat">Simmering on low code heat…</string>
    <string name="blending_in_the_ingredients_of_joy">Blending in the ingredients of joy…</string>
    <string name="peeling_back_layers_of_code">Peeling back layers of code…</string>
    <string name="fetching_the_freshest_recipes">Fetching the freshest recipes…</string>
    <string name="pouring_a_cup_of_creativity">Pouring a cup of creativity…</string>
    <string name="garnishing_your_screen">Garnishing your screen…</string>
    <string name="plating_up_the_pixels">Plating up the pixels…</string>
    <string name="serving_up_some_app_etizing_features">Serving up some app-etizing features…</string>
    <string name="cooking_up_a_storm_in_the_cloud">Cooking up a storm in the cloud…</string>
    <string name="your_patience_is_one_of_the_secret_ingredients">Your patience is one of the secret ingredients…</string>
    <string name="marinating_the_data">Marinating the data…</string>
    <string name="kneading_the_dough_of_functionality">Kneading the dough of functionality…</string>
    <string name="basting_the_firestore_database">Basting the Firestore database…</string>
    <string name="roasting_the_bugs">Roasting the bugs…</string>
    <string name="proofing_the_app">Proofing the app…</string>
    <string name="caramelizing_the_user_interface">Caramelizing the user interface…</string>
    <string name="deglazing_the_pan_of_pixels">Deglazing the pan of pixels…</string>
    <string name="zesting_up_the_app">Zesting up the app…</string>
    <string name="infusing_the_cloud_with_flavor">Infusing the cloud with flavor…</string>
    <string name="tempering_the_code">Tempering the code…</string>
    <string name="flamb_ing_the_bugs_away">Flambéing the bugs away…</string>
    <string name="folding_in_the_features">Folding in the features…</string>
    <string name="piping_in_some_sweetness">Piping in some sweetness…</string>
    <string name="dusting_the_app_with_sugar">Dusting the app with sugar…</string>
    <string name="glazing_the_user_experience">Glazing the user experience…</string>
    <string name="sifting_through_the_recipes">Sifting through the recipes…</string>
    <string name="whipping_up_some_cloud_cream">Whipping up some cloud cream…</string>
    <string name="braising_the_code_base">Braising the code base…</string>
    <string name="poaching_ideas_from_the_cloud">Poaching ideas from the cloud…</string>
    <string name="your_appetite_for_patience_is_appreciated">Your appetite for patience is appreciated…</string>
    <string name="asking_wizard_for_funny_messages">Asking Wizard for funny messages</string>
    <string name="encrypting_secret_herbs_and_spices">encrypting secret herbs and spices</string>

