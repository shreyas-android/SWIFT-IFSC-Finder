package com.jackson.android.bank.detail.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jackson.shared.domain.bankdetail.manager.BankDetailManager

class BankDetailViewModelFactory(
        private val apiKey:String,
        private val bankDetailManager : BankDetailManager,
        sharedPref : SharedPreferences): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        return BankDetailViewModel(apiKey, bankDetailManager) as T
    }
}