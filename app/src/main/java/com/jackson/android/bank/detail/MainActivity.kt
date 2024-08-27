package com.jackson.android.bank.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.paging.compose.collectAsLazyPagingItems
import com.jackson.android.bank.detail.ui.BankNavigation
import com.jackson.android.bank.detail.ui.BankScreen
import com.jackson.android.bank.detail.viewmodel.BankDetailViewModel
import com.jackson.android.bank.detail.viewmodel.BankDetailViewModelFactory
import com.jackson.shared.domain.bankdetail.manager.BankDetailManager

class MainActivity:AppCompatActivity() {

    lateinit var sharedPref : SharedPreferences
    lateinit var sharedPreferenceEditor : SharedPreferences.Editor

    companion object{

        const val PREF_NAME = "com.jackson.android.bank.detail.PREF_FILE_KEY"
        const val PREF_INITIAL_DATA_SYNC = "initial_data_sync"
        const val SWIFT_API_KEY = "RyoZnmhHWX+9ySZrxqSFcA==H2MmHi7LuzjgzmrX"
    }

    private val bankDetailManager by lazy {
        BankDetailManager.getInstance()
    }
    private val bankDetailViewModelFactory by lazy {
        BankDetailViewModelFactory(SWIFT_API_KEY,bankDetailManager, sharedPref)
    }

    private val bankDetailViewModel by lazy {
        ViewModelProvider(this, factory = bankDetailViewModelFactory)[BankDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharedPref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferenceEditor = sharedPref.edit()

        setContent {

            androidx.compose.material3.MaterialTheme(colorScheme = if(isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()){
                BankNavigation(bankDetailViewModel)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        println(sharedPref.all)
        if(!sharedPref.getBoolean(PREF_INITIAL_DATA_SYNC, false)) {
            bankDetailViewModel.updateBankFromRemote {
                sharedPreferenceEditor.putBoolean(PREF_INITIAL_DATA_SYNC, true).commit()
            }
        }else{
            bankDetailViewModel.onBankDataLoaded(false)
        }


    }
}