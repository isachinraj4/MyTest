package com.example.mytest

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytest.data.allWords
import com.example.mytest.data.days
import com.example.mytest.data.months
import com.example.mytest.ui.theme.MyComposable
import androidx.compose.material3.IconButton as IconButton1

enum class GameScreen(val route: String) {
    WordGame("start"),
    AllWords("all_words"),
    Months("months"),
    Days("days")
}

@Composable
fun HomeScreen(
    navController: NavController
) {
    val transition = rememberInfiniteTransition(label = "")
    val borderColor = if (isSystemInDarkTheme()) {
        Color.DarkGray
    } else {
        Color.LightGray
    }

//    val borderColor by transition.animateColor(
//        initialValue = Color.LightGray,
//        targetValue = Color.DarkGray,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1500),
//            repeatMode = RepeatMode.Reverse
//        ), label = ""
//    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        GameScreen.values().filter { it != GameScreen.WordGame }.forEach { screen ->
            Text(
                text = screen.name,
                modifier = Modifier
                    .clickable { navController.navigate(screen.route) }
                    .border(BorderStroke(2.dp, borderColor), RoundedCornerShape(20))
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 28.sp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopAppBar(
    currentScreen: GameScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen.name) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton1(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Arrow Button"
                    )
                }
            }
        }
    )
}

fun findScreenByRoute(route: String?): GameScreen {
    return GameScreen.values().find { it.route == route } ?: GameScreen.WordGame
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = findScreenByRoute(backStackEntry?.destination?.route)


    Scaffold(
        topBar = {
            GameTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .animateContentSize()


    ) {innerPadding ->
        NavHost(navController, startDestination = GameScreen.WordGame.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(GameScreen.WordGame.route) {
                HomeScreen(navController = navController)
            }
            composable(GameScreen.AllWords.route) {
                MyComposable(data = allWords)
            }
            composable(GameScreen.Months.route) {
                MyComposable(data = months)
            }
            composable(GameScreen.Days.route) {
                MyComposable(data = days)
            }
        }
    }
}


