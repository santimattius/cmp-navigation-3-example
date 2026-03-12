package com.santimattius.kmp.compose.examples.viewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Example 5: ViewModel scoped to the back stack entry lifecycle.
 *
 * viewModel { } + rememberViewModelStoreNavEntryDecorator() (configured in NavDisplay)
 * guarantees that:
 * - The ViewModel survives recompositions (configuration changes on Android).
 * - The ViewModel IS DESTROYED when the entry leaves the back stack (pop).
 *
 * This avoids the anti-pattern of ViewModels surviving longer than necessary
 * or being recreated on every recomposition.
 *
 * Platform-specific back gesture:
 * - Android: Native predictive back gesture.
 * - iOS: Edge pan gesture (configurable via EndEdgePanGestureBehavior).
 * - Desktop: Esc key.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScopedDetailScreen(
    itemId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // The ViewModel is scoped to the back stack entry thanks to
    // rememberViewModelStoreNavEntryDecorator() in NavDisplay.
    // Created the first time this entry is navigated to and destroyed on pop.
    val viewModel = viewModel { ItemDetailViewModel(itemId) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail (ViewModel Scoped)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Item: ${state.itemId}",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "View count: ${state.viewCount}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "ViewModel ID: ${state.viewModelId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = "The ViewModel survives recompositions but is destroyed when pressing back.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = { viewModel.incrementViewCount() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Increment view count")
            }
        }
    }
}
