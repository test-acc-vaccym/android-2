# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhy/android/sdk/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

# dontwarn first then keep class , it is work!!!!!!!!!!!!!
-dontwarn com.sun.mail.imap.protocol.**
-dontwarn com.scoreloop.client.android.core.paymentprovider.paypalx.**
-dontwarn org.apache.harmony.awt.datatransfer.**
-dontwarn org.apache.harmony.awt.**

-dontwarn javax.activation.**
-dontwarn okio.**

#-libraryjars libs/umeng-analytics-v5.2.4.jar

#三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
#-libraryjars libs/sdk-v1.0.0.jar
#-libraryjars libs/look-v1.0.1.jar

#如果不想混淆 keep 掉
-keep class com.lippi.recorder.iirfilterdesigner.** {*; }
#友盟
-keep class com.umeng.**{*;}
#项目特殊处理代码

#忽略警告
-dontwarn com.lippi.recorder.utils**

#-keep class javax.mail.**{
# *;
#}

-keep class com.sun.mail.imap.protocol.**{
*;
}

-keep class org.apache.harmony.awt.**
-keep class javax.activation.**
-keep class okio.**

-dontnote

#如果引用了v4或者v7包
-dontwarn android.support.**


-keep class javax.mail.internet.**{
  *;
}

#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }


-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontobfuscate
-repackageclasses ''
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v13.app.** { *; }
-keep interface android.support.v13.app.** { *; }
-keep class !android.support.v7.internal.view.menu.*MenuBuilder*, android.support.v7.** { *; }
-keep class !android.support.design.internal.**, ** { *; }
-allowaccessmodification