package com.santimattius.kmp.compose.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Example 1: Main module routes as @Serializable data objects/classes that
 * implement NavKey.
 *
 * Routes are simple serializable data classes. They contain no business logic.
 * NavKey is the marker that identifies an entry in the Navigation 3 back stack.
 */

@Serializable
data object HomeRoute : NavKey

@Serializable
data class DetailRoute(val itemId: String) : NavKey

@Serializable
data object SettingsRoute : NavKey
