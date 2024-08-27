package com.jackson.android.bank.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.jackson.android.bank.detail.ui.uistate.BankUISideEffect
import com.jackson.android.bank.detail.viewmodel.BankDetailViewModel
import kotlinx.coroutines.flow.collectLatest

enum class NavRoute{
    HOME,
    SWIFT_CODE
}
@Composable
fun BankNavigation(bankDetailViewModel:BankDetailViewModel) {
    val navController = rememberNavController()

    LaunchedEffect(key1 = bankDetailViewModel.bankUISideEffectFlow) {
        bankDetailViewModel.bankUISideEffectFlow.collectLatest {
            when(it){
                BankUISideEffect.NavigateSwiftCode -> {
                    navController.navigate(NavRoute.SWIFT_CODE.name)
                }
            }
        }
    }
    NavHost(navController = navController, startDestination = NavRoute.HOME.name) {

        composable(NavRoute.HOME.name) {
            BankScreen(
                bankDetailViewModel.bankUIState.collectAsState().value,
                bankDetailViewModel.bankInfoPagingFlow.collectAsLazyPagingItems(),
                bankDetailViewModel.bankDetailsPagingFlow.collectAsLazyPagingItems(),
                bankDetailViewModel.bankSwiftCodePagingFloe.collectAsLazyPagingItems(),
                bankDetailViewModel.filterBankDetailsPagingFlow.collectAsLazyPagingItems(),
                bankDetailViewModel.filterBankSwiftPagingFlow.collectAsLazyPagingItems(),
                bankDetailViewModel::setEvent)
        }

        composable(NavRoute.SWIFT_CODE.name){
            BankSwiftScreen(bankDetailViewModel.swiftCodeFilterInfoFlow.collectAsState().value,
                selectedTypeList = bankDetailViewModel.swiftSelectedTypeSetFlow.collectAsState().value,

                onNavigateBack = {
                                 navController.navigateUp()
                },
                bankDetailViewModel::setEvent)
        }
    }
}