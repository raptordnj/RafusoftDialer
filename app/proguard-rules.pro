# Retrofit
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Okio
-dontwarn okio.**

# GSON
-keep class com.google.gson.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ReactiveX
-keep class io.reactivex.rxjava3.** { *; }
-keep interface io.reactivex.rxjava3.** { *; }

-dontwarn com.google.protobuf.ExtensionRegistryLite
-dontwarn com.google.protobuf.GeneratedMessageLite
-dontwarn com.google.protobuf.Internal$ProtobufList
-dontwarn com.google.protobuf.MapFieldLite
-dontwarn com.google.protobuf.MessageLiteOrBuilder