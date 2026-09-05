package com.example.ui.theme

import android.util.Log
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

private const val TAG = "FontLoading"

// 🔒 كشف بيئة الاختبار بأمان
private val isHostUnitTest: Boolean by lazy {
    try {
        Class.forName("org.robolectric.RobolectricTestRunner") != null
    } catch (_: Throwable) {
        false
    }
}

/**
 * ✅ عائلة خطوط آمنة مع معالجة استثناءات كاملة
 * يستخدم FontLoader للتحميل الآمن
 */
val TajawalFontFamily: FontFamily by lazy {
    FontLoader.buildSafeFontFamily(isInUnitTest = isHostUnitTest).also { fontFamily ->
        if (fontFamily == FontFamily.Default) {
            Log.w(TAG, "⚠️ Using system default font - custom fonts unavailable")
            Log.w(TAG, "Error: ${FontLoader.getLastError()}")
        } else {
            Log.i(TAG, "✅ TajawalFontFamily loaded successfully")
        }
    }
}

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TajawalFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)
