package com.example.myapplication.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.myapplication.ui.home.model.HomeViewModel

private const val HomeRoute = "home"
fun NavGraphBuilder.HomeRoute(onNavigateToEdit:(studentId:Long) -> Unit,
                              onNavigateToPayment:(studentId:Long) -> Unit) {

    composable(HomeRoute){

        val homeViewModel:HomeViewModel = hiltViewModel()

        HomeScreen(studentList = homeViewModel.students, pendingAmount = homeViewModel.pendingAmount ?:0,
            sortField = homeViewModel.sortField , onSortChange = homeViewModel::onSortChange ,
            onPay = onNavigateToPayment, getPaymentAmount = homeViewModel::getPaymentAmount)
    }
}

fun NavController.navigateToHome(navOptions: NavOptions?=null) {
    this.navigate(HomeRoute, navOptions)
}