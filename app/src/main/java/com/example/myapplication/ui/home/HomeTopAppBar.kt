package com.example.myapplication.ui.home

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.home.model.SortField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(sortField: SortField, onSortChange:(SortField) ->Unit) {
    TopAppBar(title = {},
        actions = { SortDropDown(modifier = Modifier.width(120.dp),sortField = sortField, onSortChange = onSortChange)})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropDown(modifier:Modifier=Modifier,sortField: SortField, onSortChange:(SortField) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier=modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value =sortField.name,
            onValueChange = {},
            label = {  },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            SortField.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {

                        expanded = false
                        onSortChange(it)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeTopAppBarPreview() {
    HomeTopAppBar(sortField = SortField.Name, onSortChange = {})
}

