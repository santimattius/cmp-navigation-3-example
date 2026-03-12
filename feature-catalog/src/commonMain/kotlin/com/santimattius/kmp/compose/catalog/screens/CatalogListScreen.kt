package com.santimattius.kmp.compose.catalog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Product(val id: String, val name: String, val description: String)

internal val mockProducts = listOf(
    Product("1", "Kotlin Multiplatform", "Build cross-platform apps with Kotlin"),
    Product("2", "Compose Multiplatform", "Declarative UI framework for all platforms"),
    Product("3", "Navigation 3", "New navigation library for Compose"),
    Product("4", "Ktor Client", "Async HTTP client for Kotlin"),
    Product("5", "Koin", "Pragmatic DI framework for Kotlin"),
    Product("6", "Coil", "Image loading library for Compose"),
    Product("7", "Coroutines", "Asynchronous programming with Kotlin"),
    Product("8", "Serialization", "Kotlin serialization library"),
)

/**
 * Catalog list screen.
 *
 * ✅ Decoupled: receives onSelectProduct as a lambda, does not access the back stack directly.
 * Example 2 + Example 4 (list in the listPane of the adaptive layout).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogListScreen(
    onSelectProduct: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Catalog") })
        },
        modifier = modifier,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(mockProducts) { product ->
                ProductCard(
                    product = product,
                    onClick = { onSelectProduct(product.id) },
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
        )
    }
}
