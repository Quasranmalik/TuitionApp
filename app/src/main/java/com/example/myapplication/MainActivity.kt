package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import  androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.myapplication.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyApplicationTheme {
//        Greeting("Android")
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxPreview() {
    AppTheme {


    }





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownPreview() {


   AppTheme {
        Row {


    }





}


}

@OptIn(ExperimentalAnimationGraphicsApi::class,ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MyListItem() {

    var visible by remember { mutableStateOf(false) }
    Column{
        ListItem(   headlineContent = { Text(text = "I am in list")},
            leadingContent ={Icon(Icons.Default.Person, contentDescription = "p")},
            trailingContent = {
                Box (modifier = Modifier
                    .clickable { visible  = !visible }
                    .semantics { contentDescription = "dropdown" }){
                    Crossfade(targetState = visible) {expanded -> when(expanded){
                        true -> Icon(Icons.Default.KeyboardArrowUp,contentDescription = null)
                        false -> Icon(Icons.Default.KeyboardArrowDown,contentDescription = null)
                    }

                    }
                }
            }
        )
        AnimatedVisibility(visible = visible,  enter = slideInVertically { -it/2  } +
                expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f)
            ,exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {


            Surface(color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                tonalElevation = 1.dp ,
                shadowElevation = 3.dp,
                shape= RoundedCornerShape(8.dp)
            ) {
                Box {
                    FilledTonalButton(modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),onClick = {}) {
                        Text(text = "Pay")

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedVectorDrawable() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_anim)
    var atEnd by remember { mutableStateOf(false) }
    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = "Timer",
        modifier = Modifier.clickable {
            atEnd = !atEnd
        },
        contentScale = ContentScale.Crop
    )
}

//@Preview
@Composable
fun UpDownPreview() {

    UpDown()


}

@Composable
fun UpDown() {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box (modifier = Modifier
        .clickable { expanded = !expanded }
        .semantics { contentDescription = "dropdown" }){
        Crossfade(targetState = expanded) {expanded -> when(expanded){
            true -> Icon(Icons.Default.KeyboardArrowUp,contentDescription = null)
            false -> Icon(Icons.Default.KeyboardArrowDown,contentDescription = null)
        }

        }
    }
}




