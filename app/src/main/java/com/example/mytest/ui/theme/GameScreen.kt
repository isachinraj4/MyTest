@file:Suppress("UNCHECKED_CAST")

package com.example.mytest.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytest.R
import com.example.mytest.data.allWords
import com.example.mytest.ui.screens.ResOptionList
import kotlinx.coroutines.delay

@Composable
fun RadioOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val transient = rememberInfiniteTransition(label = "To create infinite transition of border around the options")
    val unSelectedColor by transient.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ), label = "This is to have infinite color transition"
    )
    val selectedColor by transient.animateColor(
        initialValue = Color(0xFF1C6402),
        targetValue = Color(0xFF00FF00),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "This is to have infinite blue color transition on option selected"
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            val gradientBorder = if (option == selectedOption) {
                BorderStroke(1.dp, selectedColor)
            } else {
                BorderStroke(1.dp, unSelectedColor)
            }
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .border(gradientBorder, RoundedCornerShape(30))
                    .clickable {
                        if (option == selectedOption) onOptionSelected("") else
                            onOptionSelected(option)
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .size(28.dp)
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.weight(1f)
                    )
                    CircularCheckButton(
                        selected = (option == selectedOption),
                        onClick = { if (option == selectedOption) onOptionSelected("") else
                            onOptionSelected(option) },
                        selectedColor
                    )
                }
            }
        }
    }
}

@Composable
fun CircularCheckButton(
    selected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    size: Dp = 28.dp
) {
    val defaultTint = Color.LightGray

    val tint = if (selected) {
        selectedColor // Always use dark green for checked state
    } else {
        defaultTint
    }

    val scale = rememberUpdatedState(if (selected) 1.2f else 1f)

    Box(
        modifier = Modifier
            .size(size)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                ),
            tint = tint
        )
    }
}




@Composable
fun Int.GameStatus(wordCount: Int, score: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.animateContentSize(),
            text = stringResource(R.string.word_count, wordCount, this@GameStatus),
            fontSize = 18.sp,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .animateContentSize(),
            text = stringResource(R.string.score, score),
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun FinalScoreDialog(
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier,
    score: Int
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {  },
        title = { Text(stringResource(R.string.congratulations)) },
        text = { Text(stringResource(R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onPlayAgain
            ) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        gameViewModel.wordCount.GameStatus(
            wordCount = gameUiState.currentWordCount,
            score = gameUiState.score
        )

        Text(
            stringResource(R.string.display_words),
            Modifier.fillMaxSize(),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        RadioOptions(
            options = gameViewModel.wordOptions,
            selectedOption = gameViewModel.userSelectedOption,
            onOptionSelected = {gameViewModel.updateUserGuess(it)}
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { gameViewModel.skipWord() }) {
                Text(text = "Skip")
            }
            Button(onClick = { gameViewModel.checkUserSelectedOption() }) {
                Text(
                    text = "Next",
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun MainScreen(
    gameViewModel: GameViewModel = viewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val showResponse = gameViewModel.showResponse
    LaunchedEffect(key1 = showResponse) {
        delay(5000)
        gameViewModel.showResponse = false
    }

    if (showResponse) {
        ResOptionList(gameViewModel = gameViewModel)
    }
    else{
        GameScreen(gameViewModel = gameViewModel)
    }

    if (gameUiState.isGameOver) {
        AnimatedVisibility(
            visible = gameUiState.isGameOver,
            enter = fadeIn() + expandHorizontally() + expandVertically(),
            exit = fadeOut() + shrinkHorizontally() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (gameUiState.isGameOver) Alignment.Center else Alignment.BottomCenter
            ) {
                FinalScoreDialog(
                    modifier= Modifier,
                    score = gameUiState.score,
                    onPlayAgain = {
                        gameViewModel.resetGame()
                    }
                )
            }
        }
    }

}

@Composable
fun MyComposable(data: List<String>) {
    val gameViewModel = viewModel<GameViewModel>(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(data) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    })

    MainScreen(gameViewModel = gameViewModel)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyTestTheme {
        MyComposable(allWords)
    }
}

