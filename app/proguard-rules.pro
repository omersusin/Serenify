# Serenify ProGuard Rules
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Compose
-dontwarn androidx.compose.**

# Room
-keep class com.serenify.app.data.** { *; }
