package com.santimattius.kmp.compose.catalog

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Example 2: Sealed interface as routes per feature module.
 *
 * Pattern: each feature module defines its own sealed interface implementing NavKey.
 * In :composeApp, all subclasses are registered with subclassesOfSealed<CatalogRoute>()
 * or manually with subclass(CatalogList::class, CatalogList.serializer()).
 *
 * Benefit: the feature module does not need to know anything about other modules.
 * :composeApp orchestrates the serialization registration.
 */
@Serializable
sealed interface CatalogRoute : NavKey

@Serializable
data object CatalogList : CatalogRoute

@Serializable
data class CatalogDetail(val productId: String) : CatalogRoute
