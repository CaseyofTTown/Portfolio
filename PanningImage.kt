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

