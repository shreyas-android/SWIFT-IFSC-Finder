package com.jackson.android.bank.detail

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import com.androidai.framework.theme.sandroid.ui.Mode
import com.androidai.framework.theme.sandroid.ui.SAndroidThemeCore
import com.androidai.framework.theme.sandroid.ui.compose.style.SAndroidUIColorCodes
import com.androidai.framework.theme.sandroid.ui.data.model.DefaultThemeInfo
import com.jackson.shared.domain.bankdetail.BankDomainAndroidCore

class BankDetailApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        BankDomainAndroidCore.init(this)

        SAndroidThemeCore.init(
            this, DefaultThemeInfo(
                Mode.DARK.value, SAndroidUIColorCodes.ColorActionViolet.toArgb(), false))


    }
}