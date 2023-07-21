package com.example.mytest.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mytest.data.MAX_NO_OF_WORDS
import com.example.mytest.data.SCORE_INCREASE
import com.example.mytest.model.Word
import com.example.mytest.model.words
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(Word())
    val uiState: StateFlow<Word> = _uiState.asStateFlow()

    //    User guess word will be stored here
    var userGuess by mutableStateOf("")
        private set

    private val start = Random.nextInt(0, words.size - 10)

    //    To store the used words in current game
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

    private fun pickRandomWordList(): List<Word> {
        return words.subList(start, start+10 )
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

    //    Update User guess
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    //    Check user guess to verify
    fun checkUserGuess() {
        if(userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
//      reset userGuess
        updateUserGuess("")
    }

    //    Function to update game state
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    word = pickRandomWordList()[start].word,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount.inc(),
                    word = pickRandomWordList()[start].word,
                    score = updatedScore
                )
            }
        }
    }

    //    Function to skip words
    fun skipWord() {
        updateGameState(_uiState.value.score )
        updateUserGuess("")
    }

    fun resetGame() {
        _uiState.value = pickRandomWordList()[start]
    }

    //    Init block to initialize the game
    init {
        resetGame()
    }
}