package com.santimattius.kmp.compose.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Profile main screen.
 *
 * ✅ Decoupled: receives onNavigateToEdit as a lambda, does not access the back stack.
 * Example 2: Part of the :feature-profile module with ProfileRoute sealed interface.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMainScreen(
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            // Avatar placeholder
            Surface(
                modifier = Modifier.size(96.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {}

            Text(
                text = "santimattius",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "santiago@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
            )

            Button(
                onClick = onNavigateToEdit,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Edit Profile")
            }
        }
    }
}
