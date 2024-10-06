package com.jackson.android.bank.detail.ui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.jackson.android.bank.detail.ui.uistate.BankUIState
import com.jackson.android.bank.detail.utils.BankProUtils
import com.jackson.android.bank.detail.utils.items
import com.jackson.shared.data.bankdetail.data.model.BankFilterInfo
import com.jackson.shared.domain.bankdetail.data.model.BankDetailInfo
import com.jackson.shared.domain.bankdetail.data.model.BankInfo
import kotlinx.coroutines.flow.collectLatest



@Composable
fun BankScreen(
        bankUIState : BankUIState, bankInfoPagingItems : LazyPagingItems<BankInfo>,
        bankDetailPagingItems : LazyPagingItems<ItemBankData>,
        bankSwiftCodePagingItems : LazyPagingItems<ItemBankData>,
        filterIFSCPagingItems : LazyPagingItems<ItemBankData>,
        filterSwiftPagingItems : LazyPagingItems<ItemBankData>, setEvent : (BankUIEvent) -> Unit) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val focusManager = LocalFocusManager.current
    LaunchedEffect(bankUIState.openDrawer) {
        if(bankUIState.openDrawer) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { drawerState.currentValue }.collectLatest {
            if(it == DrawerValue.Open) {
                setEvent(BankUIEvent.OnOpenDrawer(true))
            } else {
                focusManager.clearFocus()
                setEvent(BankUIEvent.OnOpenDrawer(false))
            }
        }
    }



    ModalNavigationDrawer(drawerState = drawerState, modifier = Modifier, drawerContent = {
        BankListDrawer(banks = bankInfoPagingItems, searchQuery = bankUIState.bankSearchQuery,
            onQueryChange = {
                setEvent(BankUIEvent.OnBankSearchQueryChanged(it))
            }, onCheckChanged = { bankItem, isChecked ->
                setEvent(BankUIEvent.OnBankInfoEnableChanged(bankItem, isChecked))
            })
    }) {
        androidx.compose.material3.Scaffold(modifier = Modifier.fillMaxWidth(), topBar = {
            val filterPagingItems = if(bankUIState.selectedScreenType == ScreenType.IFSC){
                filterIFSCPagingItems
            }else{
                filterSwiftPagingItems
            }
            EmbeddedSearchBar(bankUIState.bankDetailSearchQuery, bankUIState.selectedFilterTypeSet,
                bankUIState.selectedScreenType,
                filterPagingItems,
                bankUIState.bankFilterInfo, {
                    setEvent(BankUIEvent.OnSearchQueryChanged(it))
                }, bankUIState.isSearchActive, {
                    setEvent(BankUIEvent.OnSearchActiveChanged(it))
                }, Modifier, {
                    setEvent(BankUIEvent.OnSearchQueryChanged(it))
                }, { type, enabled ->
                    setEvent(BankUIEvent.OnFilterSelected(type, enabled))
                }, { type, value ->
                    setEvent(BankUIEvent.OnFilterValueChanged(type, value))
                }, onRemoveFilterType = {
                    setEvent(BankUIEvent.OnRemoveFilter(it))
                }, onNavigationBarClicked = {
                    setEvent(BankUIEvent.OnOpenDrawer(true))
                }, onBackButtonClicked = {
                    setEvent(BankUIEvent.OnClearAllFilter)
                }, onKeyBackPress = {
                    setEvent(BankUIEvent.OnKeyBackPress)
                }, onItemClick = {
                    setEvent(BankUIEvent.OnBankDetailClicked(it))
                }, onGetSwiftCodeClick = {
                    setEvent(BankUIEvent.OnGetSwiftCodeClicked(it))
                })
        }, content = {

            val pagingItems = if(bankUIState.selectedScreenType == ScreenType.IFSC) {
                bankDetailPagingItems
            } else {
                bankSwiftCodePagingItems
            }
            PagingBankContainer(
                Modifier
                    .fillMaxSize()
                    .padding(it), false, pagingItems,
                onItemClick = { bankDetailInfo ->
                    setEvent(BankUIEvent.OnBankDetailClicked(bankDetailInfo))
                }, onGetSwiftCodeClick = {
                    setEvent(BankUIEvent.OnGetSwiftCodeClicked(it))
                })
        }, bottomBar = {
            NavigationBar(containerColor = SAndroidUITheme.colors.sAndroidUIBackgroundColors.bottomNavigationBarColor) {

                val colors = NavigationBarItemDefaults.colors(unselectedIconColor = SAndroidUITheme
                    .colors.sAndroidUIIconColors.iconColor,
                    indicatorColor = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor.copy(alpha = 0.1f),
                    unselectedTextColor = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,
                    selectedIconColor = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor,
                    selectedTextColor = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor)
                NavigationBarItem(
                    selected = bankUIState.selectedScreenType == ScreenType.IFSC, onClick = {
                        setEvent(BankUIEvent.OnScreenTypeChanged(ScreenType.IFSC))
                    }, icon = {

                              Icon(painter = painterResource(
                                  id = R.drawable.ic_ifsc_bank),
                                  contentDescription = "", )
                    }, label = {
                        Text(
                            text = "IFSC", fontSize = 14.sp)
                    }, colors = colors)
                NavigationBarItem(
                    selected = bankUIState.selectedScreenType == ScreenType.SWIFT, onClick = {
                        setEvent(BankUIEvent.OnScreenTypeChanged(ScreenType.SWIFT))
                    }, icon = {
                        Icon(painter = painterResource(
                            id = R.drawable.ic_swift_bank),
                            contentDescription = "")
                    }, label = {
                        Text(
                            text = "SWIFT", fontSize = 14.sp)
                    }, colors = colors)
            }
        }, containerColor = SAndroidUITheme.colors.sAndroidUIBackgroundColors.backgroundColor)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EmbeddedSearchBar(
        query : String, selectedTypeList : Set<Int>,
        screenType : ScreenType,
        filteredBankDetailPagingItems : LazyPagingItems<ItemBankData>,
        bankFilterInfo : BankFilterInfo, onQueryChange : (String) -> Unit, isSearchActive : Boolean,
        onActiveChanged : (Boolean) -> Unit, modifier : Modifier = Modifier,
        onSearch : (String) -> Unit, onFilterSelected : (Int, Boolean) -> Unit,
        onValueChanged : (Int, String) -> Unit, onRemoveFilterType : (Int) -> Unit,
        onNavigationBarClicked : () -> Unit, onBackButtonClicked : () -> Unit,
        onKeyBackPress : () -> Unit, onItemClick : (BankDetailInfo) -> Unit,
        onGetSwiftCodeClick : (BankDetailInfo) -> Unit) {

    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    val searchModifier = if(isSearchActive) {
        modifier
            .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            .statusBarsPadding()

    } else {
        modifier
            .padding(start = 16.dp, top = 2.dp, end = 16.dp, bottom = 12.dp)
            .fillMaxWidth()
            .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            .statusBarsPadding()
    }

    val searchQuery = rememberSaveable(query) { mutableStateOf(query) }

    val activeChanged : (Boolean) -> Unit = { active ->
        searchQuery.value = ""
        onQueryChange("")
        onActiveChanged(active)
    }
    BankSearchBar(
        fieldContent = {

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if(isSearchActive) {
                            focusRequester.requestFocus()
                        } else {
                            onActiveChanged(true)
                        }

                    }) {
                if(isSearchActive) {
                    IconButton(
                        onClick = {
                            activeChanged(false)
                            onBackButtonClicked()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(
                                androidx.compose.ui.R.string.navigation_menu),
                            tint = SAndroidUITheme.colors.sAndroidUIIconColors.iconColor
                        )
                    }
                } else {
                    IconButton(onClick = {
                        onNavigationBarClicked()
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu",
                            tint = SAndroidUITheme.colors.sAndroidUIIconColors.iconColor)
                    }
                }
                SearchFilterChips(Modifier, selectedTypeList = selectedTypeList,
                    bankFilterInfo = bankFilterInfo, onRemoveFilterType = {
                        onRemoveFilterType(it)
                    }, inputTextField = {
                        SearchBarInputField(modifier = Modifier
                            .onKeyEvent {
                                if(it.key.keyCode == Key.Backspace.keyCode) {
                                    onKeyBackPress()
                                }
                                false
                            }
                            .width(IntrinsicSize.Min)
                            .defaultMinSize(minWidth = 36.dp),
                            query = searchQuery.value, isActive = isSearchActive,
                            onQueryChange = { query ->
                                searchQuery.value = query
                                onQueryChange(query)
                            }, onSearch = onSearch, onActiveChange = activeChanged,
                            focusRequester = focusRequester, placeholder = {
                                if(selectedTypeList.isEmpty()) {
                                    Text("Search",
                                        color = SAndroidUITheme.colors.sAndroidUITextColors.searchBarHintColor)
                                }
                            })
                    }) { type, query ->
                    onValueChanged(type, query)
                }

            }
        }, active = isSearchActive, onActiveChange = activeChanged, modifier = searchModifier,
        colors = SearchBarDefaults.colors(
            containerColor = if(isSearchActive) {
                SAndroidUITheme.colors.sAndroidUIBackgroundColors.topAppBarColor
            } else {
                SAndroidUITheme.colors.sAndroidUIBackgroundColors.searchBarBackgroundColor
            },
        ), windowInsets = if(isSearchActive) {
            SearchBarDefaults.windowInsets
        } else {
            WindowInsets(0.dp)
        }) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(SAndroidUITheme.colors.sAndroidUIBackgroundColors.backgroundColor)) {
            val (filterChipContainer, dataContainer, noResultContainer) = createRefs()
            FilterChips(Modifier.constrainAs(filterChipContainer) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints

            }, screenType = screenType, selectedFilterType = selectedTypeList) { type, enabled ->
                focusManager.clearFocus()
                onFilterSelected(type, enabled)
            }

            if(!bankFilterInfo.isEmpty(screenType == ScreenType.IFSC)) {
                if(filteredBankDetailPagingItems.itemCount == 0) {
                    Box(modifier = Modifier.constrainAs(noResultContainer) {
                        top.linkTo(filterChipContainer.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }, contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.desc_no_results),
                            fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor)
                    }
                } else {
                    PagingBankContainer(modifier = Modifier.constrainAs(dataContainer) {
                        top.linkTo(filterChipContainer.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }, true, filteredBankDetailPagingItems, onItemClick, onGetSwiftCodeClick)
                }
            }
        }
    }
}

@Composable
fun SearchChipItem(
        searchFilterType : Int, bankFilterInfo : BankFilterInfo, onRemoveFilterType : (Int) -> Unit,
        onValueChanged : (String) -> Unit) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 12.dp)
            .border(
                width = 1.dp, shape = CircleShape, color = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Text(
            text = BankProUtils.getNameByType(context = context, searchFilterType),
            fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
           color =  SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor)

        val accentColor = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor

        val customTextSelectionColors = TextSelectionColors(
            handleColor = accentColor, backgroundColor = accentColor.copy(alpha = 0.4f))
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            val keyboardOption = KeyboardOptions(imeAction = ImeAction.Search)
            val value = BankProUtils.getFilterValue(searchFilterType, bankFilterInfo)
            BasicTextField(modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(start = 6.dp)
                .defaultMinSize(minWidth = 24.dp), value = value, onValueChange = {
                onValueChanged(it)
            }, textStyle = TextStyle(
                fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor), maxLines = 1,
                keyboardActions = KeyboardActions(onSearch = {
                    onValueChanged(value)
                    focusManager.clearFocus()
                }), keyboardOptions = keyboardOption, cursorBrush = SolidColor(accentColor))

            Icon(
                modifier = Modifier.clickable {
                    onRemoveFilterType(searchFilterType)
                },
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(
                    com.google.android.material.R.string.searchview_clear_text_content_description),
                tint = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChips(
        modifier : Modifier, screenType : ScreenType, filterList : List<Int> = BankProUtils.getAllFilterList(screenType),
        selectedFilterType : Set<Int>, onFilterSelected : (Int, Boolean) -> Unit) {

    val context = LocalContext.current

    FlowRow(modifier = modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {

        filterList.forEach { type ->
            val containerColor = if(selectedFilterType.contains(type)) {
                SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor
            } else {
                SAndroidUITheme.colors.sAndroidUIBackgroundColors.secondaryBackgroundColor
            }

            val textColor = if(selectedFilterType.contains(type)) {
                SAndroidUITheme.colors.sAndroidUITextColors.highlightTextColor
            } else {
                SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor
            }
            Row(modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 6.dp)
                .background(shape = CircleShape, color = containerColor)
                .clip(CircleShape)
                .clickable {
                    onFilterSelected(type, !selectedFilterType.contains(type))
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)) {

                Text(
                    text = BankProUtils.getNameByType(context = context, type), fontSize = 16.sp,
                    fontWeight = FontWeight.Medium, color = textColor)
            }
        }

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchFilterChips(
        modifier : Modifier, selectedTypeList : Set<Int>, bankFilterInfo : BankFilterInfo,
        inputTextField : @Composable () -> Unit, onRemoveFilterType : (Int) -> Unit,
        onValueChanged : (Int, String) -> Unit) {

    FlowRow(modifier = modifier, verticalArrangement = Arrangement.Center) {

        selectedTypeList.forEach { type ->
            SearchChipItem(searchFilterType = type, bankFilterInfo = bankFilterInfo,
                onRemoveFilterType = {
                    onRemoveFilterType(it)
                }) {
                onValueChanged(type, it)
            }
        }

        inputTextField()

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagingBankContainer(
        modifier : Modifier, isSearch : Boolean, bankDetailItems : LazyPagingItems<ItemBankData>,
        onItemClick : (BankDetailInfo) -> Unit, onGetSwiftCodeClick : (BankDetailInfo) -> Unit) {

    LazyColumn(modifier = modifier) {

        for(index in 0 until bankDetailItems.itemCount) {
            when(val itemBankData = bankDetailItems.peek(index)) {
                is ItemBankData.Header -> stickyHeader {
                    Box(
                        modifier = Modifier
                            .background(SAndroidUITheme.colors.sAndroidUIBackgroundColors.backgroundColor)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = itemBankData.bankName, fontSize = 16.sp,
                            color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,
                            modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Medium)
                    }
                }

                is ItemBankData.Detail -> item {
                    BankDetailInfoCard(
                        bankDetailInfo = itemBankData.bankDetailInfo, onItemClick,
                        onGetSwiftCodeClick)
                }

                null -> {

                }
            }
        }
    }
}

@Composable
fun BankDetailInfoCard(
        bankDetailInfo : BankDetailInfo, onItemClick : (BankDetailInfo) -> Unit,
        onGetSwiftCodeClick : (BankDetailInfo) -> Unit) {
    val context = LocalContext.current
    val colors = CardDefaults.cardColors(
        containerColor = SAndroidUITheme.colors.sAndroidUIBackgroundColors.cardBackgroundColor)



    androidx.compose.material3.Card(colors = colors,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.0.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick(bankDetailInfo)
            }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Bank name : ${bankDetailInfo.bankName}", fontSize = 16.sp,
                color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,
                modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Normal)
            Text(
                text = "City : ${bankDetailInfo.city}", fontSize = 16.sp,
                color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,
                modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Normal, maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            if(bankDetailInfo.branch.isNotEmpty()) {
                Text(
                    text = "Branch : ${bankDetailInfo.branch}", fontSize = 16.sp,
                    color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,
                    modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Normal, maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }

            if(bankDetailInfo.swiftCode.isNotEmpty()) {
                Row(modifier = Modifier.padding(6.dp)) {
                    Text(text = "Swift Code : ", fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,)

                    Text(text = bankDetailInfo.swiftCode, fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,)
                    Icon(painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy",
                        tint = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                BankProUtils.copyAndShowToast(
                                    context, "SWIFT code : ${bankDetailInfo.swiftCode}")
                            })
                }

            } //}
            if(bankDetailInfo.ifscCode.isNotEmpty()) {
                Row(modifier = Modifier.padding(6.dp)) {
                    Text(text = "IFSC Code : ", fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,)
                    Text(text = bankDetailInfo.ifscCode, fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor,)
                    Icon(painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy",
                        tint = SAndroidUITheme.colors.sAndroidUIOtherColors.accentColor,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                BankProUtils.copyAndShowToast(
                                    context, "IFSC code : ${bankDetailInfo.ifscCode}")
                            })
                }
            }
        }
    }
}

@Composable
fun BankListDrawer(
        searchQuery : String, banks : LazyPagingItems<BankInfo>,
        onCheckChanged : (BankInfo, Boolean) -> Unit, onQueryChange : (String) -> Unit) {
    ModalDrawerSheet(drawerContainerColor = SAndroidUITheme.colors.sAndroidUIBackgroundColors.sideSheetBackgroundColor) {
        Surface(color = SAndroidUITheme.colors.sAndroidUIBackgroundColors.sideSheetBackgroundColor) {
            val focusRequester = remember {
                FocusRequester()
            }
            Column {

                SearchBarInputField(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(SAndroidUITheme.colors.sAndroidUIBackgroundColors.backgroundColor, shape = CircleShape)
                    .padding(start = 16.dp), query = searchQuery, isActive = false,
                    onQueryChange = {
                        onQueryChange(it)
                    }, onSearch = {
                        onQueryChange(it)
                    }, onActiveChange = {

                    }, focusRequester = focusRequester, placeholder = {
                        Text("Search banks", modifier = Modifier,  color =
                        SAndroidUITheme.colors.sAndroidUITextColors.searchBarHintColor)
                    })

                if(searchQuery.isNotEmpty() && banks.itemSnapshotList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.desc_no_results), fontSize = 16.sp, color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor)
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(start = 16.dp)) {
                        items(banks) { item ->
                            if(item != null) {
                                ConstraintLayout(modifier = Modifier
                                    .clickable {
                                        onCheckChanged(item, !item.isEnabled)
                                    }
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)) {

                                    val (checkBoxContainer, bankName, interestText, count) = createRefs()
                                    androidx.compose.material3.Checkbox(checked = item.isEnabled,
                                        onCheckedChange = {
                                            onCheckChanged(item, it)
                                        }, modifier = Modifier.constrainAs(checkBoxContainer) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                            bottom.linkTo(parent.bottom)
                                        }, colors = CheckboxDefaults.colors(checkedColor = SAndroidUITheme.colors.sAndroidUIOtherColors.checkBoxSelectedColor,
                                            uncheckedColor = SAndroidUITheme.colors.sAndroidUIOtherColors.checkBoxUnSelectedColor))

                                    Text(item.bankName, fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .constrainAs(bankName) {
                                                top.linkTo(parent.top)
                                                start.linkTo(checkBoxContainer.end)
                                                end.linkTo(count.start)
                                                bottom.linkTo(parent.bottom)
                                                width = Dimension.fillToConstraints
                                            }, textAlign = TextAlign.Start,
                                        color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor)

                                    Text(item.count.toString(), fontSize = 16.sp,
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .constrainAs(count) {
                                                top.linkTo(parent.top)
                                                end.linkTo(parent.end)
                                                bottom.linkTo(parent.bottom)
                                            }, textAlign = TextAlign.Start,
                                        color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor)

                                    val interest =
                                        if(item.minInterest != null && item.maxInterest != null) {
                                            "Savings Interest : ${item.minInterest} - ${item.maxInterest}"
                                        } else if(item.maxInterest != null) {
                                            "Savings Interest :${item.maxInterest}"
                                        } else if(item.minInterest != null) {
                                            "Savings Interest :${item.minInterest}"
                                        } else {
                                            ""
                                        }

                                    if(interest.isNotEmpty()) {
                                        Text(interest, fontSize = 14.sp,
                                            modifier = Modifier
                                                .padding(top = 2.dp, start = 8.dp)
                                                .constrainAs(interestText) {
                                                    top.linkTo(bankName.bottom)
                                                    start.linkTo(bankName.start)
                                                }, textAlign = TextAlign.Start,
                                            color = SAndroidUITheme.colors.sAndroidUITextColors.primaryTextColor)
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}