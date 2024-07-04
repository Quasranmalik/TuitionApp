package com.example.myapplication.ui.feePending

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myapplication.R

fun NavGraphBuilder.addFeePendingGraph(){
    composable(FeePendingSections.Pending.route){

    }

    composable(FeePendingSections.Upcoming.route){

    }
}



enum class FeePendingSections(
    @StringRes val title:Int,
    val route:String
){
    Pending(R.string.fee_pending,"fee/pending"),
    Upcoming(R.string.fee_upcoming,"fee/upcoming")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeePendingTopAppBar(tabs:Array<FeePendingSections>, currentRoute: String, navigateToRoute: (String) -> Unit,
                        action: @Composable () -> Unit ={}) {

    Box(modifier=Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        ){
        Row (modifier= Modifier.align(Alignment.Center)){


            val currentSection = tabs.first{it.route == currentRoute}
            SingleChoiceSegmentedButtonRow {

                tabs.forEachIndexed {index,section ->
                    SegmentedButton(selected = section == currentSection ,
                        onClick = { navigateToRoute(section.route)},
                        shape = SegmentedButtonDefaults.itemShape(index =index , count =tabs.size )) {

                        Text(stringResource( section.title) )
                    }

                }

            }

            action()

        }

    }


}

@Preview(showSystemUi = true)
@Composable
fun TopBarPreview() {

    FeePendingTopAppBar(tabs =  FeePendingSections.entries.toTypedArray() , currentRoute =FeePendingSections.Upcoming.route ,
        navigateToRoute = {},action = {DayChanger(day = 4, onDayChange = {})})
}