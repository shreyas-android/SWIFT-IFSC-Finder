package com.jackson.android.bank.detail.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.View


object ViewUtils {



    fun getSystemUIVisibility(resources: Resources): Int {
        return checkAndGetSystemUIVisibility(isNightMode(resources))
    }

    fun checkAndGetSystemUIVisibility(isNightMode: Boolean): Int {
        var systemUIVisibilityInt = 0
        if (isNightMode) {
            systemUIVisibilityInt = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View
                    .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                systemUIVisibilityInt = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View
                        .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemUIVisibilityInt = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View
                        .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
        }
        return systemUIVisibilityInt
    }

    fun isNightMode(resources: Resources): Boolean {
        return resources.configuration.uiMode
                .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}