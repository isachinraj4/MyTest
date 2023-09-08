package com.example.mytest.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mytest.data.SCORE_INCREASE
import com.example.mytest.model.Word
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class GameViewModel(private val dataSource: List<String>): ViewModel() {
    private val _uiState = MutableStateFlow(Word())
    val uiState: StateFlow<Word> = _uiState.asStateFlow()

    var userSelectedOption by  mutableStateOf("")
    var currentWord by mutableStateOf("")
    var showResponse by mutableStateOf(false)
    private var clicks by  mutableIntStateOf(0)
    private var tenWords: List<String> = emptyList()
    lateinit var wordOptions: List<String>
    var wordCount = 0


    private fun pickTenWords(): List<String> {
        val start = Random.nextInt(0, dataSource.size - 10)
        tenWords = dataSource.subList(start, start + 10)
        return tenWords
    }

    private fun getCurrentWordOption(clicks: Int): List<String> {
        if (dataSource.size <= 20 && tenWords.isEmpty()) {
            tenWords = dataSource.shuffled()
        }
        else if(dataSource.size > 20) {
            if (tenWords.isEmpty()) {
                pickTenWords()
            }
        }
        wordCount = tenWords.size
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
        val generatedWords = mutableSetOf<String>()

        result.add(word)
        generatedWords.add(word)

        // For all word lengths, generate unique options
        for (i in 1..minOf(3, len - 1)) {
            val charArray = word.toCharArray()
            val temp = charArray[len - i]
            charArray[len - i] = charArray[len - i - 1]
            charArray[len - i - 1] = temp
            val newWord = String(charArray)
            if (!generatedWords.contains(newWord)) {
                result.add(newWord)
                generatedWords.add(newWord)
            }
        }

        // Generate additional words if needed
        while (result.size < 4) {
            var newWord = word
            while (generatedWords.contains(newWord)) {
                newWord = newWord.shuffle() // A function to shuffle characters in the word randomly
            }
            result.add(newWord)
            generatedWords.add(newWord)
        }

        return result.take(4) // Make sure the list contains exactly 4 words
    }

    //  Function to shuffle the characters in word
    private fun String.shuffle(): String {
        val shuffledChars = toCharArray().apply { shuffle() }
        return String(shuffledChars)
    }



    //    Function to update game state
    private  fun updateGameState(updatedScore: Int) {
        //delay(100)
        if (currentWord == tenWords[tenWords.size - 1] ) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentWordCount = currentState.currentWordCount,
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

        return clicks
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
            showResponse = true
            updateGameState(updatedScore)
            wordOptions = getNextWords(onNextClick())
        }
        else if(userSelectedOption == "") {

        }
        else {
            showResponse = true
            updateGameState(_uiState.value.score)
            wordOptions = getNextWords(onNextClick())
        }
        updateUserGuess("")
    }

    fun resetGame() {
        tenWords = emptyList()
        clicks = 0
        _uiState.value = Word(words = getCurrentWordOption(clicks))
    }
    init {
        resetGame()
    }
}