package com.example.mytest.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytest.R
import com.example.mytest.model.Word
import com.example.mytest.model.words
import com.example.mytest.swapWords
import kotlin.random.Random


@Composable
fun RadioOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onClearSelection: () -> Unit
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
            Text("Clear Selection")
        }
    }
}


@Composable
fun DisplayWords(
    tenWords: List<Word>,
    modifier: Modifier = Modifier
) {
    var clicks by remember { mutableStateOf(0) }
    if (clicks >= tenWords.size) {
        clicks = 0
    } else if(clicks < 0) {
        clicks = tenWords.size - 1
    }
    val word = tenWords[clicks].word
    val optionsWord = swapWords(word)
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        var selectedOption by rememberSaveable { mutableStateOf("") }
        Text(text = stringResource(R.string.question_title))
        Spacer(modifier = Modifier.size(20.dp))
        com.example.mytest.RadioOptions(
            options = listOf(
                optionsWord[0],
                optionsWord[1],
                optionsWord[2],
                optionsWord[3]
            ),
            selectedOption = selectedOption,
            onOptionSelected = { option -> selectedOption = option },
            onClearSelection = { selectedOption = "" }
        )

        Spacer(modifier = Modifier.size(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { clicks -= 1 }) {
                Text(text = "Previous")
            }
            Button(onClick = { clicks += 1 }) {
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
fun GameStatus(wordCount: Int, score: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(48.dp),
    ) {
        Text(
            text = stringResource(R.string.word_count, wordCount),
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
fun LoadWords(modifier: Modifier) {
    val start = Random.nextInt(0, words.size - 10)
    com.example.mytest.DisplayWords(words.subList(start, start + 10))
}

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column() {
        GameStatus(wordCount = gameUiState.currentWordCount, score = gameUiState.score)

//        DisplayWords()

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedButton(
                onClick = { gameViewModel.skipWord() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(stringResource(R.string.skip))
            }

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 8.dp),
                onClick = { gameViewModel.checkUserGuess() }
            ) {
                Text(stringResource(R.string.submit))
            }
        }
    }
}