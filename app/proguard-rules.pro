-keep class com.example.gradecalculator.** { *; }
-keepattributes *Annotation*

# Apache POI - keep rules for Excel processing
-keep class org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-keep class org.openxmlformats.** { *; }
-keep class com.microsoft.schemas.** { *; }
-dontwarn org.apache.poi.**
-dontwarn org.apache.xmlbeans.**
-dontwarn org.openxmlformats.**
-dontwarn javax.xml.**
-dontwarn org.etsi.**
-dontwarn org.w3.**

