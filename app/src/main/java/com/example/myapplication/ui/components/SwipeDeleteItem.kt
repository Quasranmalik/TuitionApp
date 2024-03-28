package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import androidx.constraintlayout.compose.atMost

enum class DragValue {PULLED,DEFAULT}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeDeleteItem(draggableState: AnchoredDraggableState<DragValue> ,
              deleteIcon:@Composable ()->Unit,
              onDelete:() -> Unit,
              content: @Composable ()->Unit) {

    val shadow by remember {
        derivedStateOf{if (draggableState.offset >0) 2.dp else 0.dp}
    }

    var visible by remember { mutableStateOf(true) }
AnimatedVisibility(visible =visible ) {
     ConstraintLayout{


         val (surface,box) = createRefs()
         Surface(modifier= Modifier
             .offset {
                 IntOffset(
                     x=draggableState.requireOffset().toInt(),
                     y=0
                 )
             }
             .constrainAs(surface){
                 linkTo(top=box.top,bottom=box.bottom)
                 start.linkTo(box.end)
                 height= Dimension.fillToConstraints.atMost(200.dp).atLeast(20.dp)
             }
             .width(80.dp)
             ,color=MaterialTheme.colorScheme.tertiary){
             IconButton(onClick = { visible=!visible;onDelete() }) {
                 deleteIcon()
             }
         }

         Box(modifier= Modifier
             .offset {
                 IntOffset(
                     x = draggableState
                         .requireOffset()
                         .toInt(), y = 0
                 )
             }
             .constrainAs(box){}
             .fillMaxWidth()
             .shadow(shadow)
             .anchoredDraggable(
                 state = draggableState,
                 orientation = Orientation.Horizontal
             )
         ){
             content()
         }







    }
}



}

//@OptIn(ExperimentalFoundationApi::class)
//@Preview(showSystemUi = true)
//@Composable
//fun SwipeDeleteItemPreview() {
//    val draggableState = rememberAnchoredDraggableState()
//    SwipeDeleteItem(draggableState =draggableState ,
//        deleteIcon = { Icon(imageVector = Icons.Filled.Delete,contentDescription = null) },
//        onDelete = {  }) {
//        StudentItem(student= student1,onPay={})
//    }
//}




@Composable
@OptIn(ExperimentalFoundationApi::class)
fun rememberAnchoredDraggableState():AnchoredDraggableState<DragValue> {
    val density = LocalDensity.current
     return remember{
        with(density){
            DraggableAnchors {
                DragValue.PULLED at -80.dp.toPx()
                DragValue.DEFAULT at 0f
            }
        }.let{anchors ->
            AnchoredDraggableState(
                initialValue = DragValue.DEFAULT,
                anchors=anchors,
                positionalThreshold = { totalDistance: Float -> totalDistance * 0.66f },
                velocityThreshold={with(density){-120.dp.toPx()}},
                animationSpec = spring()
            )
        }

    }

}
