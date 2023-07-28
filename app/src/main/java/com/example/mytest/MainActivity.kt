package com.example.mytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytest.data.allWords
import com.example.mytest.data.days
import com.example.mytest.data.months
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
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = GameScreen.Start.route) {
                        composable(GameScreen.Start.route) {
                            HomeScreen(navController = navController)
                        }
                        composable(GameScreen.AllWords.route) {
                            AllScreen(data = allWords, navController = navController)
                        }
                        composable(GameScreen.Months.route) {
                            MonthsScreen(data = months, navController = navController)
                        }
                        composable(GameScreen.Days.route) {
                            DaysScreen(data = days, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
