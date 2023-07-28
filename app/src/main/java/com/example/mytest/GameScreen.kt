package com.example.mytest

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.mytest.ui.theme.MyComposable

enum class GameScreen(val route: String) {
    Start("start"),
    AllWords("all_words"),
    Months("months"),
    Days("days")
}

@Composable
fun HomeScreen(navController: NavController) {
    // Create a transition with target color and animation specifications
    val transition = rememberInfiniteTransition(label = "")
    val borderColor by transition.animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "AllWords",
            modifier = Modifier
                .clickable { navController.navigate(GameScreen.AllWords.route) }
                .border( BorderStroke(2.dp, borderColor), RoundedCornerShape(20) )
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(color = borderColor, fontSize = 28.sp),
        )

        Text(
            text = "Months",
            modifier = Modifier
                .clickable { navController.navigate(GameScreen.Months.route) }
                .border( BorderStroke(2.dp, borderColor), RoundedCornerShape(20) )
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(color = borderColor, fontSize = 28.sp),
        )

        Text(
            text = "Days",
            modifier = Modifier
                .clickable { navController.navigate(GameScreen.Days.route) }
                .border( BorderStroke(2.dp, borderColor), RoundedCornerShape(20) )
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(color = borderColor, fontSize = 28.sp),
        )
    }
}

@Composable
fun AllScreen(data: List<String>, navController: NavController) {
    MyComposable(data)
}

@Composable
fun MonthsScreen(data: List<String>, navController: NavController) {
    MyComposable(data)
}

@Composable
fun DaysScreen(data: List<String>, navController: NavController) {
    MyComposable(data)
}

