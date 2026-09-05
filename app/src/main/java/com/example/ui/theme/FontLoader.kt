package com.example.ui.theme

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

/**
 * نظام قوي وآمن لتحميل الخطوط المخصصة مع معالجة الأخطاء والـ Fallback
 * 
 * المشاكل التي يحلها:
 * 1. IllegalStateException: Could not load font
 * 2. Resources$NotFoundException: Font resource ID could not be retrieved
 * 3. IllegalArgumentException: Failed to create internal object. maybe invalid font data
 * 4. كراش أثناء التنقل بين الشاشات
 */

object FontLoader {
    private const val TAG = "FontLoader"
    private var fontLoadingError: String? = null
    
    /**
     * محاولة تحميل خط بشكل آمن مع معالجة الاستثناءات
     */
    fun safeLoadFont(
        fontResId: Int,
        weight: FontWeight,
        context: android.content.Context? = null
    ): Font? {
        return try {
            // تحقق من أن معرف المورد صحيح
            if (fontResId <= 0) {
                Log.w(TAG, "Invalid font resource ID: $fontResId for weight $weight")
                fontLoadingError = "Invalid resource ID: $fontResId"
                return null
            }
            
            Font(resId = fontResId, weight = weight)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load font with ID $fontResId and weight $weight", e)
            fontLoadingError = e.message ?: "Unknown error loading font"
            null
        }
    }

    /**
     * بناء عائلة خطوط آمنة مع fallback تلقائي
     */
    fun buildSafeFontFamily(isInUnitTest: Boolean = false): FontFamily {
        if (isInUnitTest) {
            Log.i(TAG, "Unit test detected - using system default font")
            return FontFamily.Default
        }

        val fonts = mutableListOf<Font>()
        var successCount = 0

        // محاولة تحميل كل وزن من أوزان الخط
        val fontConfigs = listOf(
            R.font.tajawal_regular to FontWeight.Normal,
            R.font.tajawal_medium to FontWeight.Medium,
            R.font.tajawal_medium to FontWeight.SemiBold,
            R.font.tajawal_bold to FontWeight.Bold,
            R.font.tajawal_bold to FontWeight.ExtraBold
        )

        for ((resId, weight) in fontConfigs) {
            try {
                val font = safeLoadFont(resId, weight)
                if (font != null) {
                    fonts.add(font)
                    successCount++
                    Log.d(TAG, "Successfully loaded font: weight=$weight")
                } else {
                    Log.w(TAG, "Font resource could not be loaded: weight=$weight")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading font weight=$weight: ${e.message}")
            }
        }

        return if (successCount > 0) {
            Log.i(TAG, "FontFamily created successfully with $successCount fonts loaded")
            FontFamily(fonts)
        } else {
            Log.e(TAG, "Failed to load any fonts! Falling back to system default")
            fontLoadingError = "All fonts failed to load - using system default"
            FontFamily.Default
        }
    }

    /**
     * حصول على رسالة الخطأ في حال حدوثها
     */
    fun getLastError(): String? = fontLoadingError
    
    /**
     * إعادة تعيين رسالة الخطأ
     */
    fun clearError() {
        fontLoadingError = null
    }

    /**
     * فحص صحة الخط المحمّل
     */
    fun isFontValid(fontFamily: FontFamily): Boolean {
        return fontFamily != FontFamily.Default || fontLoadingError == null
    }
}

/**
 * حل بديل: تحميل الخط بناءً على التوفر
 */
@Composable
fun getComposableFontFamily(): FontFamily {
    val context = LocalContext.current
    val isUnitTest = try {
        Class.forName("org.robolectric.RobolectricTestRunner") != null
    } catch (_: Throwable) {
        false
    }

    return try {
        FontLoader.buildSafeFontFamily(isUnitTest)
    } catch (e: Exception) {
        Log.e("FontLoader", "Exception in getComposableFontFamily", e)
        FontFamily.Default
    }
}
