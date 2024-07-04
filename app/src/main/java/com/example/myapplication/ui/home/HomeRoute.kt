package com.example.myapplication.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(onNavigateToEdit:(studentId:Long) -> Unit,
                              onNavigateToPayment:(studentId:Long) -> Unit) {

    val homeViewModel: HomeViewModel = hiltViewModel()

    val sortField by homeViewModel.sortField.collectAsStateWithLifecycle()

    HomeScreen(
        studentList = homeViewModel.students, getPendingAmount = {homeViewModel.pendingAmount},
        sortField = sortField , onSortChange = homeViewModel::onSortChange ,
        navigateToPayment = onNavigateToPayment, retrievePendingAmount = homeViewModel::retrievePendingAmount)
}

