-keep class com.example.gradecalculator.** { *; }
-keepattributes *Annotation*
-keepclasseswithmembers class * {
    @androidx.room.* <methods>;
}

