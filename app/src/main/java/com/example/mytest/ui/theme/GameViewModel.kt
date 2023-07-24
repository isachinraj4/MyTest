package com.example.mytest.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mytest.data.MAX_NO_OF_WORDS
import com.example.mytest.data.SCORE_INCREASE
import com.example.mytest.data.allWords
import com.example.mytest.model.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(Word())
    val uiState: StateFlow<Word> = _uiState.asStateFlow()

    var userSelectedOption by  mutableStateOf("")
    var currentWord by mutableStateOf("")
    var clicks by  mutableStateOf(0)
    var start = Random.nextInt(0, allWords.size - 11)
    lateinit var tenWords: List<String>
    lateinit var wordOptions: List<String>

    fun pickTenWords(): List<String> {
        return allWords.subList(start, start + 10)
    }

    fun getCurrentWordOption(): List<String> {
        tenWords = pickTenWords()
        currentWord = tenWords[clicks]
        wordOptions = swapWords(currentWord)
        return wordOptions
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

    //    Function to update game state
    private fun updateGameState(updatedScore: Int) {
        if (clicks == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    fun onNextClick(): Int {
        clicks += 1
        if (clicks >= tenWords.size) {
            clicks = 0
        } else if(clicks < 0) {
            clicks = tenWords.size - 1
        }

        return clicks;
    }

    //    Function to skip words
    fun skipWord() {
        updateGameState(_uiState.value.score )
        updateUserGuess("")
    }

    //    Update User guess
    fun updateUserGuess(guessedWord: String) {
        userSelectedOption = guessedWord
    }

    fun checkUserSelectedOption() {
        if(userSelectedOption.equals(currentWord, ignoreCase = true)) {
            val updatedScore =_uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
        onNextClick()
    }


    fun resetGame() {
        _uiState.value = Word(words = getCurrentWordOption())
    }
    init {
        resetGame()
    }
}