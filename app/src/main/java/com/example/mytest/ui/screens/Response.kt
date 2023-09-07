package com.example.mytest.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mytest.ui.theme.CircularCheckButton

@Composable
fun CrossButton(
    size: Dp = 28.dp
) {
    val defaultTint = Color.Red


    val scale = rememberUpdatedState( 1.2f )

    Box(
        modifier = Modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                ),
            tint = defaultTint
        )
    }
}

@Composable
fun ResOptionList(

) {
    val options = listOf("Test1", "Test2", "Test3", "Test4")
    val optionResponse = "Test1"
    val correctOption = "Test2"

    Column(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(50))

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        .size(28.dp)
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.weight(1f)
                    )
                    if (option == correctOption) {
                        CircularCheckButton(
                            selected = (option == correctOption),
                            onClick = {  },
                            selectedColor = Color.Green
                        )
                    }
                    else if(optionResponse == option && optionResponse != correctOption) {
                        CrossButton()
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview() {
    ResOptionList()
}