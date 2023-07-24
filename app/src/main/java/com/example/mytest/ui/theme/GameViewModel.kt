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
    private var clicks by  mutableStateOf(0)
    lateinit var tenWords: List<String>
    lateinit var wordOptions: List<String>


    private fun pickTenWords(): List<String> {
        var start = Random.nextInt(0, allWords.size - 10)
        return allWords.subList(start, start + 10)
    }

    private fun getCurrentWordOption(clicks: Int): List<String> {
        tenWords = pickTenWords()
        currentWord = tenWords[clicks]
        wordOptions = swapWords(currentWord)
        return wordOptions.shuffled()
    }

    private fun getNextWords(clicks: Int): List<String> {
        return getCurrentWordOption(clicks)
    }

    private fun swapWords(word: String): List<String> {
        val result = mutableListOf<String>()
        val len = word.length
        result.add(word)

        if (len <= 4) {
            // For word lengths <= 4, generate unique options only
            for (i in 1 until len) {
                val charArray = word.toCharArray()
                val temp = charArray[len - i]
                charArray[len - i] = charArray[len - i - 1]
                charArray[len - i - 1] = temp
                val newWord = String(charArray)
                if (newWord != word) {
                    result.add(newWord)
                }
            }
        } else {
            // For word lengths > 4, generate all possible options
            for (i in 1..3) {
                val charArray = word.toCharArray()
                val temp = charArray[len - i]
                charArray[len - i] = charArray[len - i - 1]
                charArray[len - i - 1] = temp
                result.add(String(charArray))
            }
        }

        return result
    }


    //    Function to update game state
    private fun updateGameState(updatedScore: Int) {
        if (currentWord == tenWords[tenWords.size - 1] ) {
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
                    score = updatedScore,
                )
            }
        }
    }

    private fun onNextClick(): Int {
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
        updateGameState(_uiState.value.score)
        wordOptions = getNextWords(onNextClick())
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
            wordOptions = getNextWords(onNextClick())
        } else {
            updateGameState(_uiState.value.score)
            wordOptions = getNextWords(onNextClick())
        }
        updateUserGuess("")
    }

    fun clearSelectedOption() {
        userSelectedOption = ""
    }


    fun resetGame() {
        _uiState.value = Word(words = getCurrentWordOption(clicks))
    }
    init {
        resetGame()
    }
}