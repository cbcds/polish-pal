/*
 * Copyright 2024 Darya Sizova
 *
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file has been modified by Darya Sizova to add animations for Scrollbar and code from
 * androidx/compose/foundation/gestures/TapGestureDetector.kt
 */

@file:Suppress("LongMethod", "TooManyFunctions", "MagicNumber", "ReturnCount", "InvalidPackageDeclaration")

package androidx.compose.foundation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * [CompositionLocal] used to pass [ScrollbarStyle] down the tree.
 * This value is typically set in some "Theme" composable function
 * (DesktopTheme, MaterialTheme)
 */
val LocalScrollbarStyle = staticCompositionLocalOf { defaultScrollbarStyle() }

/**
 * Defines visual style of scrollbars (thickness, shapes, colors, etc).
 * Can be passed as a parameter of scrollbar through [LocalScrollbarStyle]
 */
@Immutable
data class ScrollbarStyle(
    val minimalHeight: Dp,
    val thickness: Dp,
    val shape: Shape,
    val hoverDurationMillis: Int,
    val unhoverColor: Color,
    val hoverColor: Color,
    val fixedHeight: Boolean,
    val hideWhenIdle: Boolean,
)

/**
 * Simple default [ScrollbarStyle] without applying MaterialTheme.
 */
fun defaultScrollbarStyle() = ScrollbarStyle(
    minimalHeight = 16.dp,
    thickness = 8.dp,
    shape = RoundedCornerShape(4.dp),
    hoverDurationMillis = 300,
    unhoverColor = Color.Black.copy(alpha = 0.12f),
    hoverColor = Color.Black.copy(alpha = 0.50f),
    fixedHeight = false,
    hideWhenIdle = true,
)

/**
 * Vertical scrollbar that can be attached to some scrollable
 * component (ScrollableColumn, LazyColumn) and share common state with it.
 *
 * Can be placed independently.
 *
 * Example:
 *     val state = rememberScrollState(0f)
 *
 *     Box(Modifier.fillMaxSize()) {
 *         Box(modifier = Modifier.verticalScroll(state)) {
 *             ...
 *         }
 *
 *         VerticalScrollbar(
 *             Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
 *             rememberScrollbarAdapter(state)
 *         )
 *     }
 *
 * @param adapter [ScrollbarAdapter] that will be used to communicate with scrollable component
 * @param modifier the modifier to apply to this layout
 * @param reverseLayout reverse the direction of scrolling and layout, when `true`
 * and [LazyListState.firstVisibleItemIndex] == 0 then scrollbar
 * will be at the bottom of the container.
 * It is usually used in pair with `LazyColumn(reverseLayout = true)`
 * @param style [ScrollbarStyle] to define visual style of scrollbar
 * @param interactionSource [MutableInteractionSource] that will be used to dispatch
 * [DragInteraction.Start] when this Scrollbar is being dragged.
 */
@Composable
fun VerticalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    style: ScrollbarStyle = LocalScrollbarStyle.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) = Scrollbar(
    adapter,
    modifier,
    reverseLayout,
    style,
    interactionSource,
    isVertical = true
)

/**
 * Horizontal scrollbar that can be attached to some scrollable
 * component (Modifier.verticalScroll(), LazyRow) and share common state with it.
 *
 * Can be placed independently.
 *
 * Example:
 *     val state = rememberScrollState(0f)
 *
 *     Box(Modifier.fillMaxSize()) {
 *         Box(modifier = Modifier.verticalScroll(state)) {
 *             ...
 *         }
 *
 *         HorizontalScrollbar(
 *             Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
 *             rememberScrollbarAdapter(state)
 *         )
 *     }
 *
 * @param adapter [ScrollbarAdapter] that will be used to communicate with scrollable component
 * @param modifier the modifier to apply to this layout
 * @param reverseLayout reverse the direction of scrolling and layout, when `true`
 * and [LazyListState.firstVisibleItemIndex] == 0 then scrollbar
 * will be at the end of the container.
 * It is usually used in pair with `LazyRow(reverseLayout = true)`
 * @param style [ScrollbarStyle] to define visual style of scrollbar
 * @param interactionSource [MutableInteractionSource] that will be used to dispatch
 * [DragInteraction.Start] when this Scrollbar is being dragged.
 */
@Composable
fun HorizontalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    style: ScrollbarStyle = LocalScrollbarStyle.current,
    interactionSource: MutableInteractionSource? = null,
) = Scrollbar(
    adapter,
    modifier,
    if (LocalLayoutDirection.current == LayoutDirection.Rtl) !reverseLayout else reverseLayout,
    style,
    interactionSource,
    isVertical = false
)

@Composable
private fun Scrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean,
    style: ScrollbarStyle,
    interactionSource: MutableInteractionSource?,
    isVertical: Boolean
) = with(LocalDensity.current) {
    val dragInteraction = remember { mutableStateOf<DragInteraction.Start?>(null) }
    DisposableEffect(interactionSource) {
        onDispose {
            dragInteraction.value?.let { interaction ->
                interactionSource?.tryEmit(DragInteraction.Cancel(interaction))
                dragInteraction.value = null
            }
        }
    }

    var containerSize by remember { mutableStateOf(0) }
    val isHovered by interactionSource?.collectIsHoveredAsState() ?: mutableStateOf(false)

    val isHighlighted by remember {
        derivedStateOf {
            isHovered || dragInteraction.value is DragInteraction.Start
        }
    }

    val minimalHeight = style.minimalHeight.toPx()
    val sliderAdapter = remember(adapter, containerSize, minimalHeight, reverseLayout) {
        SliderAdapter(adapter, containerSize, minimalHeight, style.fixedHeight, reverseLayout)
    }

    val scrollThickness = style.thickness.roundToPx()
    val measurePolicy = if (isVertical) {
        remember(sliderAdapter, scrollThickness) {
            verticalMeasurePolicy(sliderAdapter, { containerSize = it }, scrollThickness)
        }
    } else {
        remember(sliderAdapter, scrollThickness) {
            horizontalMeasurePolicy(sliderAdapter, { containerSize = it }, scrollThickness)
        }
    }

    val color by animateColorAsState(
        if (isHighlighted) style.hoverColor else style.unhoverColor,
        animationSpec = TweenSpec(durationMillis = style.hoverDurationMillis)
    )

    val scrollbarIsDragged by interactionSource?.collectIsDraggedAsState() ?: mutableStateOf(false)
    val scrollableIsDragged by adapter.interactionSource.collectIsDraggedAsState()
    val isVisible by remember {
        derivedStateOf {
            scrollableIsDragged || scrollbarIsDragged
        }
    }

    if (style.hideWhenIdle) {
        AnimatedVisibility(
            visible = isVisible,
            enter = EnterTransition.None,
            exit = fadeOut(
                animationSpec = keyframes {
                    durationMillis = 3000
                    1f at 0
                    1f at 2700 using LinearOutSlowInEasing
                }
            ),
            modifier = modifier,
        ) {
            Scrollbar(
                color,
                style.shape,
                isVertical,
                dragInteraction,
                sliderAdapter,
                measurePolicy,
                interactionSource,
            )
        }
    } else {
        Scrollbar(
            color,
            style.shape,
            isVertical,
            dragInteraction,
            sliderAdapter,
            measurePolicy,
            interactionSource,
            modifier,
        )
    }
}

@Composable
private fun Scrollbar(
    color: Color,
    shape: Shape,
    isVertical: Boolean,
    dragInteraction: MutableState<DragInteraction.Start?>,
    sliderAdapter: SliderAdapter,
    measurePolicy: MeasurePolicy,
    interactionSource: MutableInteractionSource?,
    modifier: Modifier = Modifier,
) {
    Layout(
        {
            Box(
                Modifier
                    .background(color, shape)
                    .let { modifier ->
                        interactionSource?.let {
                            modifier.scrollbarDrag(
                                interactionSource = interactionSource,
                                draggedInteraction = dragInteraction,
                                onDelta = { offset ->
                                    sliderAdapter.rawPosition += if (isVertical) offset.y else offset.x
                                },
                                onFinished = { sliderAdapter.rawPosition = sliderAdapter.position }
                            )
                        } ?: modifier
                    }
            )
        },
        modifier = interactionSource
            ?.let { modifier.hoverable(interactionSource = interactionSource) }
            ?: modifier,
        measurePolicy = measurePolicy,
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.scrollbarDrag(
    interactionSource: MutableInteractionSource,
    draggedInteraction: MutableState<DragInteraction.Start?>,
    onDelta: (Offset) -> Unit,
    onFinished: () -> Unit
): Modifier = composed {
    val currentInteractionSource by rememberUpdatedState(interactionSource)
    val currentDraggedInteraction by rememberUpdatedState(draggedInteraction)
    val currentOnDelta by rememberUpdatedState(onDelta)
    val currentOnFinished by rememberUpdatedState(onFinished)
    pointerInput(Unit) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            val interaction = DragInteraction.Start()
            currentInteractionSource.tryEmit(interaction)
            currentDraggedInteraction.value = interaction
            val isSuccess = drag(down.id) { change ->
                currentOnDelta.invoke(change.positionChange())
                change.consume()
            }
            val finishInteraction = if (isSuccess) {
                DragInteraction.Stop(interaction)
            } else {
                DragInteraction.Cancel(interaction)
            }
            currentInteractionSource.tryEmit(finishInteraction)
            currentDraggedInteraction.value = null
            currentOnFinished.invoke()
        }
    }
}

/**
 * Create and [remember] [ScrollbarAdapter] for scrollable container and current instance of
 * [scrollState]
 */
@Composable
fun rememberScrollbarAdapter(
    scrollState: ScrollState
): ScrollbarAdapter = remember(scrollState) {
    ScrollbarAdapter(scrollState)
}

/**
 * Create and [remember] [ScrollbarAdapter] for lazy scrollable container and current instance of
 * [scrollState]
 */
@Composable
fun rememberScrollbarAdapter(
    scrollState: LazyListState,
): ScrollbarAdapter {
    return remember(scrollState) {
        ScrollbarAdapter(scrollState)
    }
}

/**
 * ScrollbarAdapter for Modifier.verticalScroll and Modifier.horizontalScroll
 *
 * [scrollState] is instance of [ScrollState] which is used by scrollable component
 *
 * Example:
 *     val state = rememberScrollState(0f)
 *
 *     Box(Modifier.fillMaxSize()) {
 *         Box(modifier = Modifier.verticalScroll(state)) {
 *             ...
 *         }
 *
 *         VerticalScrollbar(
 *             Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
 *             rememberScrollbarAdapter(state)
 *         )
 *     }
 */
fun ScrollbarAdapter(
    scrollState: ScrollState
): ScrollbarAdapter = ScrollableScrollbarAdapter(scrollState)

private class ScrollableScrollbarAdapter(
    private val scrollState: ScrollState
) : ScrollbarAdapter {
    override val interactionSource: InteractionSource
        get() = scrollState.interactionSource

    override val scrollOffset: Float get() = scrollState.value.toFloat()

    override suspend fun scrollTo(containerSize: Int, scrollOffset: Float) {
        scrollState.scrollTo(scrollOffset.roundToInt())
    }

    override fun maxScrollOffset(containerSize: Int) =
        scrollState.maxValue.toFloat()
}

/**
 * ScrollbarAdapter for lazy lists.
 *
 * [scrollState] is instance of [LazyListState] which is used by scrollable component
 *
 * Scrollbar size and position will be dynamically changed on the current visible content.
 *
 * Example:
 *     Box(Modifier.fillMaxSize()) {
 *         val state = rememberLazyListState()
 *
 *         LazyColumn(state = state) {
 *             ...
 *         }
 *
 *         VerticalScrollbar(
 *             Modifier.align(Alignment.CenterEnd),
 *             rememberScrollbarAdapter(state)
 *         )
 *     }
 */
fun ScrollbarAdapter(
    scrollState: LazyListState
): ScrollbarAdapter = LazyScrollbarAdapter(
    scrollState
)

private class LazyScrollbarAdapter(
    private val scrollState: LazyListState
) : ScrollbarAdapter {
    override val interactionSource: InteractionSource
        get() = scrollState.interactionSource

    override val scrollOffset: Float
        get() = scrollState.firstVisibleItemIndex * averageItemSize +
            scrollState.firstVisibleItemScrollOffset

    override suspend fun scrollTo(containerSize: Int, scrollOffset: Float) {
        val distance = scrollOffset - this@LazyScrollbarAdapter.scrollOffset

        // if we scroll less than containerSize we need to use scrollBy function to avoid
        // undesirable scroll jumps (when an item size is different)
        //
        // if we scroll more than containerSize we should immediately jump to this position
        // without recreating all items between the current and the new position
        if (abs(distance) <= containerSize) {
            scrollState.scrollBy(distance)
        } else {
            snapTo(containerSize, scrollOffset)
        }
    }

    private suspend fun snapTo(containerSize: Int, scrollOffset: Float) {
        val maximumValue = maxScrollOffset(containerSize).toDouble()
        val scrollOffsetCoerced = scrollOffset.toDouble().coerceIn(0.0, maximumValue)
        val averageItemSize = averageItemSize.toDouble()

        val index = (scrollOffsetCoerced / averageItemSize)
            .toInt()
            .coerceAtLeast(0)
            .coerceAtMost(itemCount - 1)

        val offset = (scrollOffsetCoerced - index * averageItemSize)
            .toInt()
            .coerceAtLeast(0)

        scrollState.scrollToItem(index = index, scrollOffset = offset)
    }

    override fun maxScrollOffset(containerSize: Int) =
        (averageItemSize * itemCount - containerSize).coerceAtLeast(0f)

    private val itemCount get() = scrollState.layoutInfo.totalItemsCount

    private val averageItemSize by derivedStateOf {
        scrollState
            .layoutInfo
            .visibleItemsInfo
            .asSequence()
            .map { it.size }
            .average()
            .toFloat()
    }
}

/**
 * Defines how to scroll the scrollable component
 */
interface ScrollbarAdapter {
    val interactionSource: InteractionSource

    /**
     * Scroll offset of the content inside the scrollable component.
     * Offset "100" means that the content is scrolled by 100 pixels from the start.
     */
    val scrollOffset: Float

    /**
     * Instantly jump to [scrollOffset] in pixels
     *
     * @param containerSize size of the scrollable container
     *  (for example, it is height of ScrollableColumn if we use VerticalScrollbar)
     * @param scrollOffset target value in pixels to jump to,
     *  value will be coerced to 0..maxScrollOffset
     */
    suspend fun scrollTo(containerSize: Int, scrollOffset: Float)

    /**
     * Maximum scroll offset of the content inside the scrollable component
     *
     * @param containerSize size of the scrollable component
     *  (for example, it is height of ScrollableColumn if we use VerticalScrollbar)
     */
    fun maxScrollOffset(containerSize: Int): Float
}

private class SliderAdapter(
    val adapter: ScrollbarAdapter,
    val containerSize: Int,
    val minHeight: Float,
    val fixedHeight: Boolean,
    val reverseLayout: Boolean,
) {
    private val contentSize get() = adapter.maxScrollOffset(containerSize) + containerSize
    private val visiblePart get() = containerSize.toFloat() / contentSize

    val size get() = if (fixedHeight) {
        minHeight.coerceAtMost(containerSize.toFloat())
    } else {
        (containerSize * visiblePart)
            .coerceAtLeast(minHeight)
            .coerceAtMost(containerSize.toFloat())
    }

    private val scrollScale: Float
        get() {
            val extraScrollbarSpace = containerSize - size
            val extraContentSpace = contentSize - containerSize
            return if (extraContentSpace == 0f) 1f else extraScrollbarSpace / extraContentSpace
        }

    /**
     * A position with cumulative offset, may be out of the container when dragging
     */
    var rawPosition: Float = position
        set(value) {
            field = value
            position = value
        }

    /**
     * Actual scroll of content regarding slider layout
     */
    private var scrollPosition: Float
        get() = scrollScale * adapter.scrollOffset
        set(value) {
            runBlocking {
                adapter.scrollTo(containerSize, value / scrollScale)
            }
        }

    /**
     * Actual position of a thumb within slider container
     */
    var position: Float
        get() = if (reverseLayout) containerSize - size - scrollPosition else scrollPosition
        set(value) {
            scrollPosition = if (reverseLayout) {
                containerSize - size - value
            } else {
                value
            }
        }

    val bounds get() = position..position + size
}

private fun verticalMeasurePolicy(
    sliderAdapter: SliderAdapter,
    setContainerSize: (Int) -> Unit,
    scrollThickness: Int
) = MeasurePolicy { measurables, constraints ->
    setContainerSize(constraints.maxHeight)
    val height = sliderAdapter.size.toInt()
    val placeable = measurables.first().measure(
        Constraints.fixed(
            constraints.constrainWidth(scrollThickness),
            height
        )
    )
    layout(placeable.width, constraints.maxHeight) {
        placeable.place(0, sliderAdapter.position.toInt())
    }
}

private fun horizontalMeasurePolicy(
    sliderAdapter: SliderAdapter,
    setContainerSize: (Int) -> Unit,
    scrollThickness: Int
) = MeasurePolicy { measurables, constraints ->
    setContainerSize(constraints.maxWidth)
    val width = sliderAdapter.size.toInt()
    val placeable = measurables.first().measure(
        Constraints.fixed(
            width,
            constraints.constrainHeight(scrollThickness)
        )
    )
    layout(constraints.maxWidth, placeable.height) {
        placeable.place(sliderAdapter.position.toInt(), 0)
    }
}

/**
 * Receiver scope for [detectTapGestures]'s `onPress` lambda. This offers
 * two methods to allow waiting for the press to be released.
 */
private interface PressGestureScope : Density {
    /**
     * Waits for the press to be released before returning. If the gesture was canceled by
     * motion being consumed by another gesture, [GestureCancellationException] will be
     * thrown.
     */
    suspend fun awaitRelease()

    /**
     * Waits for the press to be released before returning. If the press was released,
     * `true` is returned, or if the gesture was canceled by motion being consumed by
     * another gesture, `false` is returned .
     */
    suspend fun tryAwaitRelease(): Boolean
}

private val NoPressGesture: suspend PressGestureScope.(Offset) -> Unit = { }

/**
 * Detects tap, double-tap, and long press gestures and calls [onTap], [onDoubleTap], and
 * [onLongPress], respectively, when detected. [onPress] is called when the press is detected
 * and the [PressGestureScope.tryAwaitRelease] and [PressGestureScope.awaitRelease] can be
 * used to detect when pointers have released or the gesture was canceled.
 * The first pointer down and final pointer up are consumed, and in the
 * case of long press, all changes after the long press is detected are consumed.
 *
 * Each function parameter receives an [Offset] representing the position relative to the containing
 * element. The [Offset] can be outside the actual bounds of the element itself meaning the numbers
 * can be negative or larger than the element bounds if the touch target is smaller than the
 * [ViewConfiguration.minimumTouchTargetSize].
 *
 * When [onDoubleTap] is provided, the tap gesture is detected only after
 * the [ViewConfiguration.doubleTapMinTimeMillis] has passed and [onDoubleTap] is called if the
 * second tap is started before [ViewConfiguration.doubleTapTimeoutMillis]. If [onDoubleTap] is not
 * provided, then [onTap] is called when the pointer up has been received.
 *
 * After the initial [onPress], if the pointer moves out of the input area, the position change
 * is consumed, or another gesture consumes the down or up events, the gestures are considered
 * canceled. That means [onDoubleTap], [onLongPress], and [onTap] will not be called after a
 * gesture has been canceled.
 *
 * If the first down event is consumed somewhere else, the entire gesture will be skipped,
 * including [onPress].
 */
private suspend fun PointerInputScope.detectTapGestures(
    onDoubleTap: ((Offset) -> Unit)? = null,
    onLongPress: ((Offset) -> Unit)? = null,
    onPress: suspend PressGestureScope.(Offset) -> Unit = NoPressGesture,
    onTap: ((Offset) -> Unit)? = null
) = coroutineScope {
    // special signal to indicate to the sending side that it shouldn't intercept and consume
    // cancel/up events as we're only require down events
    val pressScope = PressGestureScopeImpl(this@detectTapGestures)

    awaitEachGesture {
        val down = awaitFirstDown()
        down.consume()
        launch {
            pressScope.reset()
        }
        if (onPress !== NoPressGesture) {
            launch {
                pressScope.onPress(down.position)
            }
        }
        val longPressTimeout = onLongPress?.let {
            viewConfiguration.longPressTimeoutMillis
        } ?: (Long.MAX_VALUE / 2)
        var upOrCancel: PointerInputChange? = null
        try {
            // wait for first tap up or long press
            upOrCancel = withTimeout(longPressTimeout) {
                waitForUpOrCancellation()
            }
            if (upOrCancel == null) {
                launch {
                    pressScope.cancel() // tap-up was canceled
                }
            } else {
                upOrCancel.consume()
                launch {
                    pressScope.release()
                }
            }
        } catch (_: PointerEventTimeoutCancellationException) {
            onLongPress?.invoke(down.position)
            consumeUntilUp()
            launch {
                pressScope.release()
            }
        }

        if (upOrCancel != null) {
            // tap was successful.
            if (onDoubleTap == null) {
                onTap?.invoke(upOrCancel.position) // no need to check for double-tap.
            } else {
                // check for second tap
                val secondDown = awaitSecondDown(upOrCancel)

                if (secondDown == null) {
                    onTap?.invoke(upOrCancel.position) // no valid second tap started
                } else {
                    // Second tap down detected
                    launch {
                        pressScope.reset()
                    }
                    if (onPress !== NoPressGesture) {
                        launch { pressScope.onPress(secondDown.position) }
                    }

                    @Suppress("SwallowedException")
                    try {
                        // Might have a long second press as the second tap
                        withTimeout(longPressTimeout) {
                            val secondUp = waitForUpOrCancellation()
                            if (secondUp != null) {
                                secondUp.consume()
                                launch {
                                    pressScope.release()
                                }
                                onDoubleTap(secondUp.position)
                            } else {
                                launch {
                                    pressScope.cancel()
                                }
                                onTap?.invoke(upOrCancel.position)
                            }
                        }
                    } catch (e: PointerEventTimeoutCancellationException) {
                        // The first tap was valid, but the second tap is a long press.
                        // notify for the first tap
                        onTap?.invoke(upOrCancel.position)

                        // notify for the long press
                        onLongPress?.invoke(secondDown.position)
                        consumeUntilUp()
                        launch {
                            pressScope.release()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Consumes all pointer events until nothing is pressed and then returns. This method assumes
 * that something is currently pressed.
 */
private suspend fun AwaitPointerEventScope.consumeUntilUp() {
    do {
        val event = awaitPointerEvent()
        event.changes.fastForEach { it.consume() }
    } while (event.changes.fastAny { it.pressed })
}

/**
 * Waits for [ViewConfiguration.doubleTapTimeoutMillis] for a second press event. If a
 * second press event is received before the time out, it is returned or `null` is returned
 * if no second press is received.
 */
private suspend fun AwaitPointerEventScope.awaitSecondDown(
    firstUp: PointerInputChange
): PointerInputChange? = withTimeoutOrNull(viewConfiguration.doubleTapTimeoutMillis) {
    val minUptime = firstUp.uptimeMillis + viewConfiguration.doubleTapMinTimeMillis
    var change: PointerInputChange
    // The second tap doesn't count if it happens before DoubleTapMinTime of the first tap
    do {
        change = awaitFirstDown()
    } while (change.uptimeMillis < minUptime)
    change
}

/**
 * Reads events in the given [pass] until all pointers are up or the gesture was canceled.
 * The gesture is considered canceled when a pointer leaves the event region, a position
 * change has been consumed or a pointer down change event was already consumed in the given
 * pass. If the gesture was not canceled, the final up change is returned or `null` if the
 * event was canceled.
 */
private suspend fun AwaitPointerEventScope.waitForUpOrCancellation(
    pass: PointerEventPass = PointerEventPass.Main
): PointerInputChange? {
    while (true) {
        val event = awaitPointerEvent(pass)
        if (event.changes.fastAll { it.changedToUp() }) {
            // All pointers are up
            return event.changes[0]
        }

        if (event.changes.fastAny {
                it.isConsumed || it.isOutOfBounds(size, extendedTouchPadding)
            }
        ) {
            return null // Canceled
        }

        // Check for cancel by position consumption. We can look on the Final pass of the
        // existing pointer event because it comes after the pass we checked above.
        val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
        if (consumeCheck.changes.fastAny { it.isConsumed }) {
            return null
        }
    }
}

/**
 * [detectTapGestures]'s implementation of [PressGestureScope].
 */
private class PressGestureScopeImpl(
    density: Density
) : PressGestureScope, Density by density {
    private var isReleased = false
    private var isCanceled = false
    private val mutex = Mutex(locked = false)

    /**
     * Called when a gesture has been canceled.
     */
    fun cancel() {
        isCanceled = true
        mutex.unlock()
    }

    /**
     * Called when all pointers are up.
     */
    fun release() {
        isReleased = true
        mutex.unlock()
    }

    /**
     * Called when a new gesture has started.
     */
    suspend fun reset() {
        mutex.lock()
        isReleased = false
        isCanceled = false
    }

    override suspend fun awaitRelease() {
        if (!tryAwaitRelease()) {
            throw GestureCancellationException("The press gesture was canceled.")
        }
    }

    override suspend fun tryAwaitRelease(): Boolean {
        if (!isReleased && !isCanceled) {
            mutex.lock()
            mutex.unlock()
        }
        return isReleased
    }
}
