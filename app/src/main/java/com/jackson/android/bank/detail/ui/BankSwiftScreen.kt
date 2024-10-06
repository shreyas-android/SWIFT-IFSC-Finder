package com.jackson.android.bank.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.LazyPagingItems
import com.androidai.framework.theme.sandroid.ui.SAndroidUITheme
import com.jackson.android.bank.detail.R
import com.jackson.android.bank.detail.data.enums.ScreenType
import com.jackson.android.bank.detail.data.model.ItemBankData
import com.jackson.android.bank.detail.ui.uistate.BankUIEvent
import com.jackson.android.bank.detail.utils.BankProUtils
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.data.bankdetail.data.model.SwiftCodeFilterInfo
import migrations.BankSwift

@Composable
fun BankSwiftScreen(
        swiftCodeFilterInfo : SwiftCodeFilterInfo,
        selectedTypeList : Set<Int>, onNavigateBack : () -> Unit,
        setEvent : (BankUIEvent) -> Unit) {

    val city = swiftCodeFilterInfo.city
    val bankName = swiftCodeFilterInfo.bankName

    val focusManager = LocalFocusManager.current


    ConstraintLayout(
        modifier = Modifier
            .background(SAndroidUITheme.colors.sAndroidUIBackgroundColors.backgroundColor)
            .fillMaxSize()
            .navigationBarsPadding()) {
        val (topAppBar, divider, filterChipContainer, dataContainer, noResultContainer) = createRefs()

        Surface(
            modifier = Modifier
                .defaultMinSize(minHeight = 72.dp)
                .fillMaxWidth()
                .constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }, color = SAndroidUITheme.colors.sAndroidUIBackgroundColors.topAppBarColor,
            contentColor = contentColorFor(SAndroidUITheme.colors.sAndroidUIBackgroundColors.topAppBarColor),
            tonalElevation = 6.0.dp) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.statusBarsPadding())
            {
                IconButton(onClick = {
                    onNavigateBack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Arrow back",
                        tint = SAndroidUITheme.colors.sAndroidUIIconColors.iconColor)
                }
                SearchFilterChips(Modifier.fillMaxWidth(), selectedTypeList, BankFilterInfo(
                    bankName = bankName, city = city, branch = swiftCodeFilterInfo.branch),
                    inputTextField = {

                    }, onRemoveFilterType = {
                        setEvent(BankUIEvent.OnSwiftRemoveFilter(it))
                    }, onValueChanged = { type, value ->
                        setEvent(BankUIEvent.OnSwiftFilterValueChanged(type, value))
                    })

            }
        }

        Divider(modifier = Modifier
            .constrainAs(divider) {
                top.linkTo(topAppBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
            .fillMaxWidth()
            .height(0.5.dp), color = SAndroidUITheme.colors.sAndroidUIOtherColors.dividerColor)

        FilterChips(Modifier
            .constrainAs(filterChipContainer) {
                top.linkTo(divider.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints

            }
            .background(SAndroidUITheme.colors.sAndroidUIBackgroundColors.topAppBarColor),  ScreenType.SWIFT, BankProUtils.getBankSwiftFilter(),
            selectedTypeList) { type, enabled ->
            focusManager.clearFocus()
            setEvent(BankUIEvent.OnSwiftFilterSelected(type, enabled))
        }
    }
}