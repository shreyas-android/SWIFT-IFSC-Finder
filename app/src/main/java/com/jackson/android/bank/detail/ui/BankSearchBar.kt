package com.jackson.android.bank.detail.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.MutableWindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Composable
fun BankSearchBar(
        fieldContent:@Composable () -> Unit,
        active: Boolean,
        onActiveChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        shape: Shape = SearchBarDefaults.inputFieldShape,
        colors: SearchBarColors = SearchBarDefaults.colors(),
        tonalElevation: Dp = SearchBarDefaults.Elevation,
        windowInsets: WindowInsets = SearchBarDefaults.windowInsets,
        content: @Composable ColumnScope.() -> Unit,
) {
    val animationProgress: State<Float> = animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = if (active) AnimationEnterFloatSpec else AnimationExitFloatSpec, label = "")

    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    val defaultInputFieldShape = SearchBarDefaults.inputFieldShape
    val defaultFullScreenShape = SearchBarDefaults.fullScreenShape
    val useFullScreenShape by remember {
        derivedStateOf(structuralEqualityPolicy()) { animationProgress.value == 1f }
    }
    val animatedShape = remember(useFullScreenShape, shape) {
        when {
            shape == defaultInputFieldShape ->
                // The shape can only be animated if it's the default spec value
                GenericShape { size, _ ->
                    val radius = with(density) {
                        (SearchBarCornerRadius * (1 - animationProgress.value)).toPx()
                    }
                    addRoundRect(RoundRect(size.toRect(), CornerRadius(radius)))
                }
            useFullScreenShape -> defaultFullScreenShape
            else -> shape
        }
    }


    val unconsumedInsets = remember { MutableWindowInsets() }
    val topPadding = remember(density) {
        derivedStateOf {
            SearchBarVerticalPadding +
                    unconsumedInsets.asPaddingValues(density).calculateTopPadding()
        }
    }

    Surface(
        shape = animatedShape,
        color = colors.containerColor,
        contentColor = contentColorFor(colors.containerColor),
        tonalElevation = tonalElevation,
        modifier = modifier
            .zIndex(1f)
            .onConsumedWindowInsetsChanged { consumedInsets ->
                unconsumedInsets.insets = windowInsets.exclude(consumedInsets)
            }
            .consumeWindowInsets(unconsumedInsets)
            .layout { measurable, constraints ->
                val animatedTopPadding =
                    lerp(topPadding.value, 0.dp, animationProgress.value).roundToPx()

                val startWidth = max(constraints.minWidth, SearchBarMinWidth.roundToPx())
                    .coerceAtMost(min(constraints.maxWidth, SearchBarMaxWidth.roundToPx()))
                val startHeight = max(constraints.minHeight, SearchBarDefaults.InputFieldHeight.roundToPx())
                    .coerceAtMost(constraints.maxHeight)
                val endWidth = constraints.maxWidth
                val endHeight = constraints.maxHeight

                val width = lerp(startWidth.toDp(), endWidth.toDp(), animationProgress.value)
                val height =
                    lerp(startHeight.toDp(), endHeight.toDp(), animationProgress.value) + animatedTopPadding.toDp()



                val placeable = measurable.measure(
                    Constraints.fixed(width.toPx().toInt(), height.toPx().toInt())
                    .offset(vertical = -animatedTopPadding))
                layout(width.toPx().toInt(), height.toPx().toInt()) {
                    placeable.placeRelative(0, animatedTopPadding)
                }
            }
    ) {
        Column {
            fieldContent()

            val showResults by remember {
                derivedStateOf(structuralEqualityPolicy()) { animationProgress.value > 0 }
            }
            if (showResults) {
                Column(Modifier.graphicsLayer { alpha = animationProgress.value }) {
                    HorizontalDivider(color = colors.dividerColor)
                    content()
                }
            }
        }
    }

    LaunchedEffect(active) {
        if (!active) {
            delay(AnimationDelayMillis.toLong())
            focusManager.clearFocus()
        }
    }

    BackHandler(enabled = active) {
        onActiveChange(false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarInputField(
        query: String,
        isActive:Boolean,
        onQueryChange: (String) -> Unit,
        onSearch: (String) -> Unit,
        onActiveChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        focusRequester : FocusRequester,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        colors: TextFieldColors = SearchBarDefaults.inputFieldColors(),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val customTextSelectionColors = TextSelectionColors(
        handleColor = accentColor,
        backgroundColor = accentColor.copy(alpha = 0.4f))
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        val focusManager = LocalFocusManager.current
        BasicTextField(value = query, onValueChange = onQueryChange,
            modifier = modifier.focusRequester(focusRequester)
                .onFocusChanged { if(it.isFocused) onActiveChange(true) }
                .then(if(isActive) Modifier else Modifier), enabled = enabled,
            singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                onSearch(query)
            }), interactionSource = interactionSource,
            cursorBrush = SolidColor(accentColor),
            textStyle = TextStyle(fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface),
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = query,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon?.let { leading ->
                        {
                            Box(Modifier.offset(x = SearchBarIconOffsetX)) { leading() }
                        }
                    },
                    trailingIcon = trailingIcon?.let { trailing ->
                        {
                            Box(Modifier.offset(x = -SearchBarIconOffsetX)) { trailing() }
                        }
                    },
                    shape = SearchBarDefaults.inputFieldShape,
                    colors = colors,
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                    container = {},
                )
            })
    }
}


// Animation specs
private const val AnimationEnterDurationMillis: Int =600
private const val AnimationExitDurationMillis: Int = 350
private const val AnimationDelayMillis: Int = 100
private val AnimationEnterEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
private val AnimationExitEasing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f)
private val AnimationEnterFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationEnterDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationEnterEasing,
)
private val AnimationExitFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationExitDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationExitEasing,
)
private val AnimationEnterSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationEnterDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationEnterEasing,
)
private val AnimationExitSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationExitDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationExitEasing,
)
private val DockedEnterTransition: EnterTransition =
    fadeIn(AnimationEnterFloatSpec) + expandVertically(AnimationEnterSizeSpec)
private val DockedExitTransition: ExitTransition =
    fadeOut(AnimationExitFloatSpec) + shrinkVertically(AnimationExitSizeSpec)

private val SearchBarCornerRadius: Dp = 56.0.dp / 2
internal val DockedActiveTableMinHeight: Dp = 240.dp
private const val DockedActiveTableMaxHeightScreenRatio: Float = 2f / 3f
internal val SearchBarMinWidth: Dp = 360.dp
private val SearchBarMaxWidth: Dp = 720.dp
internal val SearchBarVerticalPadding: Dp = 8.dp
// Search bar has 16dp padding between icons and start/end, while by default text field has 12dp.
private val SearchBarIconOffsetX: Dp = 4.dp


@Stable
private class AnimatedPaddingValues(
        val animationProgress: State<Float>,
        val topPadding: State<Dp>,
) : PaddingValues {
    override fun calculateTopPadding(): Dp = topPadding.value * animationProgress.value
    override fun calculateBottomPadding(): Dp = 8.dp * animationProgress.value

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp = 0.dp
    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp = 0.dp
}