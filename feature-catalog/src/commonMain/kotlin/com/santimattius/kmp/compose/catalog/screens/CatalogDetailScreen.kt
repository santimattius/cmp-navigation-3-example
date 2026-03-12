package com.santimattius.kmp.compose.catalog.screens

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
 * Catalog detail screen.
 *
 * ✅ Decoupled: receives onBack as a lambda.
 * Example 2 + Example 4 (detail in the detailPane of the adaptive layout).
 *
 * On small screens (phone): displayed as full screen.
 * On large screens (tablet/desktop): displayed in the right panel alongside the list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogDetailScreen(
    productId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val product = mockProducts.find { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Product Detail") },
                navigationIcon = {
                    // On large screens with adaptive layout, the back button
                    // may be hidden since the list is always visible
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (product != null) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Product ID: ${product.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            } else {
                Text(
                    text = "Product not found: $productId",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
