package com.example.myapplication.ui.feePending.pending

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PendingRoute(onNavigateToRoute:(String) ->Unit,onNavigateToPayment:(Long) -> Unit) {

    val pendingViewModel:PendingViewModel =  hiltViewModel()

    PendingScreen(
        students = pendingViewModel.students,
        retrievePendingAmount = pendingViewModel::retrievePendingAmount,
        getPendingAmount = {pendingViewModel.pendingAmount},
        onNavigateToRoute = onNavigateToRoute,
        navigateToPayment = onNavigateToPayment
    )
}