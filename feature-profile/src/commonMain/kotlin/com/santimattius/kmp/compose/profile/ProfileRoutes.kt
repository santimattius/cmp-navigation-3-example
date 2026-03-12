package com.santimattius.kmp.compose.profile

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Example 2: Sealed interface as routes per feature module.
 *
 * Pattern: each feature module defines its own sealed interface implementing NavKey.
 * Routes are registered in :composeApp with subclassesOfSealed<ProfileRoute>() or
 * manually with subclass(ProfileMain::class, ProfileMain.serializer()).
 *
 * This pattern scales well with DI (Koin, Kotlin Inject) and allows each feature
 * module to register its routes independently.
 */
@Serializable
sealed interface ProfileRoute : NavKey

@Serializable
data object ProfileMain : ProfileRoute

@Serializable
data object ProfileEdit : ProfileRoute
