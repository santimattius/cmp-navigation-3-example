package com.santimattius.kmp.compose.examples.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
 * Example 1: Settings screen.
 *
 * ✅ Decoupled: receives onBack as a lambda.
 * The back stack is managed exclusively by AppNavigation.kt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "App version: 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Navigation 3 version: 1.0.0-alpha05",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Compose Multiplatform: 1.10.x",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
