# Add project-specific ProGuard rules here.
# Conservative defaults; tighten when we hit obfuscation problems.

# Compose
-keep class androidx.compose.runtime.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# OkHttp / Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

# Sentry
-keep class io.sentry.** { *; }
