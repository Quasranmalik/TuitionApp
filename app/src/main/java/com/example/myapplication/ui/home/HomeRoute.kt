package com.example.myapplication.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.home.model.HomeViewModel

@Composable
fun HomeRoute(onNavigateToEdit:(studentId:Long) -> Unit,
                              onNavigateToPayment:(studentId:Long) -> Unit) {

    val homeViewModel:HomeViewModel = hiltViewModel()

    HomeScreen(studentList = homeViewModel.students, pendingAmount = homeViewModel.pendingAmount ?:0,
        sortField = homeViewModel.sortField , onSortChange = homeViewModel::onSortChange ,
        onPay = onNavigateToPayment, getPaymentAmount = homeViewModel::getPaymentAmount)
}

