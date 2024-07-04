package com.example.myapplication.ui.feePending

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DayChanger( modifier:Modifier=Modifier,day:Int, onDayChange:(Int) -> Unit) {

    Row(modifier=modifier,verticalAlignment = Alignment.CenterVertically) {
        DecreaseButton(onClick={onDayChange(day-1)})
        Card(shape = RoundedCornerShape(30)){
            Text(modifier=Modifier.padding(horizontal = 5.dp),
                text = "$day",
                style = MaterialTheme.typography.titleLarge)
        }
        IncreaseButton(onClick={onDayChange(day+1)})

    }
}

@Composable
private fun DecreaseButton(onClick : () -> Unit) {
    IconButton(onClick = onClick){
        Icon(contentDescription = null, imageVector = Icons.Filled.RemoveCircle)
    }
}

@Composable
fun IncreaseButton(onClick : () -> Unit) {
    IconButton(onClick = onClick){
        Icon(contentDescription = null,imageVector = Icons.Filled.AddCircle)
    }

}

@Preview(showSystemUi = true)
@Composable
fun DayChangerPreview() {
    DayChanger(day = 5, onDayChange = {})
}