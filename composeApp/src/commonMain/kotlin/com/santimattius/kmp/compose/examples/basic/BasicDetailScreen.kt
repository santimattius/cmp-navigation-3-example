package com.santimattius.kmp.compose.examples.basic

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Example 1: Detail screen with basic navigation.
 *
 * ✅ Correct pattern: receives navigation lambdas, does NOT access the back stack directly.
 *
 * ❌ Anti-pattern (coupled):
 *   fun DetailScreen(backStack: SnapshotStateList<NavKey>) {
 *       Button(onClick = { backStack.add(SettingsRoute) }) { ... }
 *   }
 *
 * ✅ Correct pattern (decoupled):
 *   fun DetailScreen(itemId: String, onNavigateToSettings: () -> Unit, onBack: () -> Unit) {
 *       Button(onClick = onNavigateToSettings) { ... }
 *   }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDetailScreen(
    itemId: String,
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
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
                text = "Item Detail",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Item ID: $itemId",
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Go to Settings")
            }
        }
    }
}
