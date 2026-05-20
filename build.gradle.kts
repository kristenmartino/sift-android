// Top-level — plugins applied at module level via libs.versions.toml aliases.
// Wrapper version (`./gradlew --version`) is pinned to 8.14.3, JDK 21.
// All version refs live in gradle/libs.versions.toml — bump there, never here.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.spotless) apply false
}
