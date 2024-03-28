package com.example.myapplication.ui.studentEdit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAppBar(new:Boolean,edit:Boolean, onNavigateBack:()->Unit, onEdit:() -> Unit,onSave:() ->Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    CenterAlignedTopAppBar(title = {Text(text = if(new) "New Student" else if (edit) "Edit" else "")},
        navigationIcon = {IconButton(onClick =  onNavigateBack ) {
                Icon(imageVector = Icons.Filled.ArrowBack,contentDescription = "cancel")
            }
        },
        actions= {if (edit) {TextButton(onClick = {onNavigateBack();onSave()}  ){Text(text="Save")}}
        else {IconButton(onClick = onEdit) {
            Icon(imageVector = Icons.Filled.Edit,contentDescription = "edit")
        }}}
        , scrollBehavior = scrollBehavior)

}

@Preview
@Composable
fun DialogAppBarPreview() {
    var edit by remember{ mutableStateOf(false) }

    DialogAppBar( new = true ,edit = edit,onEdit={edit = true}, onNavigateBack = {},onSave={})

}

