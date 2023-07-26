package com.example.mytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mytest.data.allWords
import com.example.mytest.ui.theme.GameScreen
import com.example.mytest.ui.theme.GameViewModel
import com.example.mytest.ui.theme.MyComposable
import com.example.mytest.ui.theme.MyTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyComposable(allWords)
//                    Not best practice to initialize the viewModel
//                    GameScreen(gameViewModel = GameViewModel(allWords))
                }
            }
        }
    }
}
