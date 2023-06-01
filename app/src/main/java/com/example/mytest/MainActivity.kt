package com.example.mytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mytest.data.WordDatasource
import com.example.mytest.model.Word
import com.example.mytest.model.words
import com.example.mytest.ui.theme.MyTestTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}

@Composable
fun RadioOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = CenterVertically,
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
    }
}


@Composable
fun DisplayWords(
    modifier: Modifier = Modifier,
    allWord: List<Word>
) {
    val random = Random
    val subWordList = allWord.subList(
        random.nextInt(0, 10),
        random.nextInt(20,30)
    )
    var index = 0
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        var selectedOption by rememberSaveable { mutableStateOf("") }
        Text(text = stringResource(R.string.question_title))
        Spacer(modifier = Modifier.size(20.dp))
        RadioOptions(
            options = listOf(
                subWordList[index].word,
                subWordList[index].word,
                subWordList[index].word,
                subWordList[index].word
            ),
            selectedOption = selectedOption,
            onOptionSelected = { option -> selectedOption = option }
        )

        Spacer(modifier = Modifier.size(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Previous")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = "Next",
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun swapWords(word: String): List<String> {
    var result =  mutableListOf<String>()
    val len = word.length
    result.add(word)
    for( i in 1..3) {
        val charArray = word.toCharArray()
        val temp = charArray[len - i]
        charArray[len - i] = charArray[len - i - 1]
        charArray[len - i - 1] = temp
        result.add(String(charArray))
    }
    return result
}

@Composable
fun LoadWords(modifier: Modifier) {
    DisplayWords(allWord = words)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyTestTheme {
        LoadWords(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp))
    }
}