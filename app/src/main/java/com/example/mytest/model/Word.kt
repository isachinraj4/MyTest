package com.example.mytest.model

data class Word(val words: List<String> = listOf(""),
                val isGuessedWordWrong: Boolean = false,
                val score: Int = 0,
                val currentWordCount: Int = 0,
                val isGameOver: Boolean = false
)