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
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.paging.compose.collectAsLazyPagingItems
import com.androidai.framework.theme.sandroid.ui.SAndroidThemeCore
import com.androidai.framework.theme.sandroid.ui.SAndroidUITheme
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
        BankDetailViewModelFactory(BuildConfig.SWIFT_API_KEY,bankDetailManager, sharedPref)
    }

    private val bankDetailViewModel by lazy {
        ViewModelProvider(this, factory = bankDetailViewModelFactory)[BankDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        sharedPref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferenceEditor = sharedPref.edit()

        setContent {

            val themeManager = remember {
                SAndroidThemeCore.getThemeManager()
            }
            SAndroidUITheme(themeManager) {
                BankNavigation(bankDetailViewModel)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if(!sharedPref.getBoolean(PREF_INITIAL_DATA_SYNC, false)) {
            bankDetailViewModel.updateBankFromRemote {
                sharedPreferenceEditor.putBoolean(PREF_INITIAL_DATA_SYNC, true).commit()
            }
        }else{
            bankDetailViewModel.onBankDataLoaded(false)
        }


    }
}