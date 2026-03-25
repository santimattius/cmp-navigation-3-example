package com.santimattius.kmp.compose.examples.predictiveback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlin.math.roundToInt

/**
 * Example 6: Screen wrapper that handles the predictive back gesture using the
 * Navigation Event API (`navigationevent-compose`).
 *
 * ✅ Replaces the deprecated `PredictiveBackHandler()` with the new
 *    `NavigationBackHandler` + hoisted `NavigationEventState`.
 *
 * Key rules:
 *  - Navigation ONLY happens in [onBackCompleted] (single navigation point).
 *  - Visual rollback is centralised in [onBackCancelled] (reset [offsetPx] to 0f).
 *  - The [LaunchedEffect] observer only updates the visual offset; it never navigates.
 *  - No `try/catch` is used as control flow.
 *
 * @param onNavigateBack Callback invoked when the back gesture is confirmed.
 * @param modifier Optional [Modifier] applied to the outer [Box].
 * @param content Screen content rendered inside the animated [Box].
 */
@Composable
fun ScreenWithPredictiveBack(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    // Hoisted state for the back gesture – starts with no active event.
    val navState = rememberNavigationEventState(NavigationEventInfo.None)

    // Horizontal offset (in px) applied to the content during the swipe animation.
    var offsetPx by remember { mutableStateOf(0f) }

    // Register the back handler:
    //  - onBackCompleted → confirm navigation (single navigation point).
    //  - onBackCancelled → reset the visual offset (centralised rollback).
    NavigationBackHandler(
        state = navState,
        isBackEnabled = true,
        onBackCancelled = {
            offsetPx = 0f
        },
        onBackCompleted = {
            onNavigateBack()
        },
    )

    // Observe transitionState to drive the swipe animation.
    // Each progress update produces a new InProgress instance, restarting this effect.
    // Navigation is intentionally NOT triggered here.
    LaunchedEffect(navState.transitionState) {
        val ts = navState.transitionState
        if (ts is NavigationEventTransitionState.InProgress) {
            offsetPx = ts.latestEvent.progress * 40f
        }
    }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetPx.roundToInt(), 0) }
            .fillMaxSize(),
        content = content,
    )
}
