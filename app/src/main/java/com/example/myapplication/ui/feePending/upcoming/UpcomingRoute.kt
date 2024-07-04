package com.example.myapplication.ui.feePending.upcoming

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun UpcomingRoute(onNavigateToRoute: (String) -> Unit, onNavigateToPayment: (Long) -> Unit) {
    val upcomingViewModel:UpcomingViewModel = hiltViewModel()
    val upcomingDays by upcomingViewModel.upcomingDays.collectAsStateWithLifecycle()

    UpcomingScreen(
        students = upcomingViewModel.students,
        getUpcomingDays = {upcomingDays },
        onUpcomingDaysChange = upcomingViewModel::onChangeUpcomingDays,
        onNavigateToRoute = onNavigateToRoute,
        navigateToPayment = onNavigateToPayment,
        getPendingAmount = { upcomingViewModel.pendingAmount },
        retrievePendingAmount = upcomingViewModel::retrievePendingAmount)

}