package com.example.mytest.ui.theme

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytest.R
import com.example.mytest.data.allWords


@Composable
fun RadioOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onClearSelection: () -> Unit,
    onKeyboardDone: () -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Button to clear the selection
        Button(
            onClick = { onClearSelection() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Clear")
        }
    }
}

@Composable
fun GameStatus(wordCount: Int, score: Int,word_count: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(48.dp),
    ) {
        Text(
            text = stringResource(R.string.word_count, wordCount, word_count),
            fontSize = 18.sp,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
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
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
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
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GameStatus(wordCount = gameUiState.currentWordCount, score = gameUiState.score, word_count = gameViewModel.wordCount)

        Text(
            stringResource(R.string.display_words),
            Modifier.fillMaxSize(),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Toast.makeText(LocalContext.current,gameViewModel.tenWords.toString(),Toast.LENGTH_LONG).show()
        RadioOptions(
            options = gameViewModel.wordOptions,
            selectedOption = gameViewModel.userSelectedOption,
            onOptionSelected = {gameViewModel.updateUserGuess(it)},
            onClearSelection = {gameViewModel.clearSelectedOption()},
            onKeyboardDone = { gameViewModel.checkUserSelectedOption() }
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

        if (gameUiState.isGameOver) {
            FinalScoreDialog(
                score = gameUiState.score,
                onPlayAgain = { gameViewModel.resetGame() }
            )
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

    GameScreen(gameViewModel = gameViewModel)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyTestTheme {
        MyComposable(allWords)
    }
}

