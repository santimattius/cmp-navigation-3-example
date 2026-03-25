package com.santimattius.kmp.compose.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.santimattius.kmp.compose.catalog.CatalogDetail
import com.santimattius.kmp.compose.catalog.CatalogList
import com.santimattius.kmp.compose.catalog.screens.CatalogDetailScreen
import com.santimattius.kmp.compose.catalog.screens.CatalogListScreen
import com.santimattius.kmp.compose.examples.basic.BasicSettingsScreen
import com.santimattius.kmp.compose.examples.predictiveback.ScreenWithPredictiveBack
import com.santimattius.kmp.compose.examples.viewmodel.ScopedDetailScreen
import com.santimattius.kmp.compose.features.home.HomeScreen
import com.santimattius.kmp.compose.profile.ProfileEdit
import com.santimattius.kmp.compose.profile.ProfileMain
import com.santimattius.kmp.compose.profile.screens.ProfileEditScreen
import com.santimattius.kmp.compose.profile.screens.ProfileMainScreen

/**
 * Example 3: Bottom navigation with independent back stack per tab.
 *
 * Each tab maintains its own SnapshotStateList<NavKey> with rememberNavBackStack,
 * so that switching tabs preserves the navigation state of each section.
 *
 * rememberNavBackStack is used with navSerializationConfig to guarantee
 * correct restoration on iOS and Web (Example 1 - polymorphic serialization).
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation() {
    var activeTab by remember { mutableStateOf(AppTab.HOME) }

    // Each tab has its own back stack with state restoration
    val homeBackStack = rememberNavBackStack(navSerializationConfig, HomeRoute)
    val catalogBackStack = rememberNavBackStack(navSerializationConfig, CatalogList)
    val profileBackStack = rememberNavBackStack(navSerializationConfig, ProfileMain)

    val currentBackStack = when (activeTab) {
        AppTab.HOME -> homeBackStack
        AppTab.CATALOG -> catalogBackStack
        AppTab.PROFILE -> profileBackStack
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = activeTab == tab,
                        onClick = { activeTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        /**
         * Example 4: Adaptive layout with SceneStrategy for the Catalog tab.
         *
         * rememberListDetailSceneStrategy() is called unconditionally (Compose rule).
         * When Catalog entries have ListDetailSceneStrategy.listPane() /
         * detailPane() in their metadata, they are displayed side-by-side on large screens.
         * Home and Profile entries without that metadata are shown in single pane.
         *
         * Example 5: rememberViewModelStoreNavEntryDecorator() scopes ViewModels
         * to the lifecycle of each back stack entry: created on entry, destroyed on
         * exit (pop). They survive recompositions but NOT tab switches.
         */
        val catalogSceneStrategy = rememberListDetailSceneStrategy<NavKey>()

        NavDisplay(
            backStack = currentBackStack,
            onBack = { currentBackStack.removeLastOrNull() },
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            sceneStrategy = catalogSceneStrategy,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {

                // --- Example 1: Basic navigation (Home tab) ---

                entry<HomeRoute> {
                    HomeScreen(
                        onNavigateToDetail = { id -> homeBackStack.add(DetailRoute(id)) },
                        onNavigateToSettings = { homeBackStack.add(SettingsRoute) },
                    )
                }

                entry<DetailRoute> { key ->
                    // Example 5: ViewModel scoped to the entry via entryDecorators
                    ScopedDetailScreen(
                        itemId = key.itemId,
                        onBack = { homeBackStack.removeLastOrNull() },
                    )
                }

                // --- Example 6: Predictive back with Navigation Event ---
                entry<SettingsRoute> {
                    ScreenWithPredictiveBack(
                        onNavigateBack = { homeBackStack.removeLastOrNull() },
                    ) {
                        BasicSettingsScreen(
                            onBack = { homeBackStack.removeLastOrNull() },
                        )
                    }
                }

                // --- Example 2 + 4: Catalog with adaptive layout ---

                entry<CatalogList>(
                    metadata = ListDetailSceneStrategy.listPane(),
                ) {
                    CatalogListScreen(
                        onSelectProduct = { id -> catalogBackStack.add(CatalogDetail(id)) },
                    )
                }

                entry<CatalogDetail>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) { key ->
                    CatalogDetailScreen(
                        productId = key.productId,
                        onBack = { catalogBackStack.removeLastOrNull() },
                    )
                }

                // --- Example 2: Profile feature module ---

                entry<ProfileMain> {
                    ProfileMainScreen(
                        onNavigateToEdit = { profileBackStack.add(ProfileEdit) },
                    )
                }

                entry<ProfileEdit> {
                    ProfileEditScreen(
                        onBack = { profileBackStack.removeLastOrNull() },
                    )
                }
            },
        )
    }
}

/**
 * Bottom navigation tabs.
 * Each tab has its own independent back stack.
 */
enum class AppTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    CATALOG("Catalog", Icons.Default.ShoppingCart),
    PROFILE("Profile", Icons.Default.Person),
}
