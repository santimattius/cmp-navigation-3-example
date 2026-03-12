package com.santimattius.kmp.compose.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.santimattius.kmp.compose.catalog.CatalogDetail
import com.santimattius.kmp.compose.catalog.CatalogList
import com.santimattius.kmp.compose.profile.ProfileEdit
import com.santimattius.kmp.compose.profile.ProfileMain
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Example 1 + Example 2: Polymorphic serialization configuration for Nav3.
 *
 * REQUIRED for iOS and Web: without this configuration, rememberNavBackStack()
 * cannot restore the back stack state on platforms other than Android.
 *
 * Anti-pattern to avoid:
 *   ❌ val backStack = rememberNavBackStack(HomeRoute)
 *      → Compiles on Android but FAILS on iOS/Web (without SavedStateConfiguration)
 *
 *   ✅ val backStack = rememberNavBackStack(navSerializationConfig, HomeRoute)
 *
 * IMPORTANT: every route registered here MUST be annotated with @Serializable.
 * If a new route is added and forgotten to register, there will be a runtime crash
 * that is hard to debug (especially on iOS).
 */
val navSerializationConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            // Main module routes (Example 1)
            subclass(HomeRoute::class, HomeRoute.serializer())
            subclass(DetailRoute::class, DetailRoute.serializer())
            subclass(SettingsRoute::class, SettingsRoute.serializer())

            // :feature-catalog routes (Example 2)
            // Recommended pattern with sealed interface: register each subclass.
            // Nav3 offers subclassesOfSealed<CatalogRoute>() as a helper (alpha).
            subclass(CatalogList::class, CatalogList.serializer())
            subclass(CatalogDetail::class, CatalogDetail.serializer())

            // :feature-profile routes (Example 2)
            subclass(ProfileMain::class, ProfileMain.serializer())
            subclass(ProfileEdit::class, ProfileEdit.serializer())
        }
    }
}
