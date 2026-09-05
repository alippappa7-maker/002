package com.example.ui.theme

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle

private const val TAG = "Theme"

private val DarkColorScheme = darkColorScheme(
    primary = NaturalDarkOlive,
    onPrimary = Color.Black,
    primaryContainer = NaturalDarkBorder,
    onPrimaryContainer = NaturalDarkText,
    secondary = NaturalDarkOliveDark,
    onSecondary = Color.White,
    tertiary = ModernAccentAmber,
    background = NaturalDarkBg,
    onBackground = NaturalDarkText,
    surface = NaturalDarkSurface,
    onSurface = NaturalDarkText,
    surfaceVariant = NaturalDarkItemBg,
    onSurfaceVariant = NaturalDarkMuted,
    outline = NaturalDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = ModernIndigo,
    onPrimary = Color.White,
    primaryContainer = ModernItemBg,
    onPrimaryContainer = ModernCharcoal,
    secondary = ModernIndigoDark,
    onSecondary = Color.White,
    tertiary = ModernAccentAmber,
    background = ModernSlateBg,
    onBackground = ModernCharcoal,
    surface = ModernCardBg,
    onSurface = ModernCharcoal,
    surfaceVariant = ModernItemBg,
    onSurfaceVariant = ModernMuted,
    outline = ModernBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontScale: Float = 1.0f,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    // 🔒 كاش ColorScheme لتجنب إعادة الحسابات
    val colorScheme = remember(darkTheme, dynamicColor) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                // Dynamic colors يُحسبها في Composable (غير آمن)
                // لذا نستخدم الألوان الثابتة بدلاً منها
                if (darkTheme) DarkColorScheme else LightColorScheme
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    }

    val appColors = remember(darkTheme) {
        if (darkTheme) DarkThemeColors else LightThemeColors
    }

    // 🛡️ تحميل الخط مرة واحدة فقط
    val fontFamily = remember { TajawalFontFamily }

    try {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides TextStyle(fontFamily = fontFamily),
                LocalAppColors provides appColors,
                LocalFontScale provides fontScale
            ) {
                content()
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "❌ Error in theme composition", e)
        // Fallback: عرض المحتوى بدون theme مخصص
        try {
            MaterialTheme(
                colorScheme = colorScheme
            ) {
                content()
            }
        } catch (fallbackError: Exception) {
            Log.e(TAG, "❌ Even fallback theme failed", fallbackError)
            // إذا فشل كل شيء، عرض المحتوى بدون theme
            content()
        }
    }
}
