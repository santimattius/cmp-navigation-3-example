package com.santimattius.kmp.compose.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BedtimeOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.santimattius.kmp.compose.core.ui.components.AppBar
import com.santimattius.kmp.compose.core.ui.components.ErrorView
import com.santimattius.kmp.compose.core.ui.components.LoadingIndicator
import com.santimattius.kmp.compose.core.ui.components.NetworkImage
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home tab main screen.
 *
 * ✅ Decoupled: receives navigation lambdas.
 * The ViewModel is obtained via Koin to demonstrate DI integration.
 *
 * @param onNavigateToDetail Lambda to navigate to item detail (Example 1 + 5).
 * @param onNavigateToSettings Lambda to navigate to Settings (Example 1).
 */
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onRefresh = viewModel::randomImage,
        onDarkMode = viewModel::darkMode,
        onNavigateToDetail = { onNavigateToDetail(state.data?.id ?: "1") },
        onNavigateToSettings = onNavigateToSettings,
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onRefresh: () -> Unit,
    onDarkMode: () -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(title = "CMP Navigation", actions = {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    )
                }
                IconButton(onClick = onDarkMode) {
                    Icon(
                        if (state.isDarkMode) Icons.Default.BedtimeOff else Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    )
                }
            })
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = onRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = null)
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> LoadingIndicator()

                state.data == null || state.hasError -> {
                    ErrorView(message = "An error occurred while updating the image")
                }

                else -> {
                    Card(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        onClick = onNavigateToDetail,
                    ) {
                        NetworkImage(
                            imageUrl = state.data.url,
                            contentDescription = "Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .aspectRatio(ratio = (16 / 8).toFloat()),
                        )
                    }
                }
            }
        }
    }
}
