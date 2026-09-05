package com.example.ui.theme

import android.util.Log
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

/**
 * 🔒 نظام قوي وآمن لتحميل الخطوط المخصصة
 * 
 * يحل المشاكل:
 * - IllegalStateException: Could not load font
 * - Resources$NotFoundException: Font resource ID could not be retrieved  
 * - IllegalArgumentException: Failed to create internal object
 * - كراش أثناء onMeasure في Compose
 * - كراش عند التنقل بين الشاشات
 */

object FontLoader {
    private const val TAG = "FontLoader"
    private var loadingError: String? = null
    private var isInitialized = false
    
    /**
     * محاولة تحميل خط واحد بشكل آمن
     * @return Font إذا نجح، null إذا فشل
     */
    private fun safeTryLoadFont(fontResId: Int, weight: FontWeight, name: String): Font? {
        return try {
            // تحقق من أن معرف المورد صحيح (يجب أن يكون > 0)
            if (fontResId <= 0) {
                Log.w(TAG, "❌ Invalid font resource ID: $fontResId for weight $weight")
                return null
            }
            
            // محاولة إنشاء كائن Font
            val font = Font(resId = fontResId, weight = weight)
            Log.d(TAG, "✓ Font loaded successfully: $name (weight=$weight, resId=$fontResId)")
            font
        } catch (e: Exception) {
            Log.e(TAG, "✗ Failed to load font $name - weight=$weight, resId=$fontResId", e)
            loadingError = "Font load failed: $name, error=${e.message}"
            null
        }
    }

    /**
     * بناء عائلة خطوط آمنة مع fallback تلقائي
     * إذا فشل تحميل جميع الخطوط، ترجع FontFamily.Default
     */
    fun buildSafeFontFamily(isInUnitTest: Boolean = false): FontFamily {
        // في الاختبارات، استخدم الخط الافتراضي
        if (isInUnitTest) {
            Log.i(TAG, "🧪 Unit test environment detected - using system default font")
            isInitialized = true
            return FontFamily.Default
        }

        try {
            val fonts = mutableListOf<Font>()
            var successCount = 0

            // قائمة أوزان الخط المراد تحميلها
            val fontConfigs = listOf(
                Triple(R.font.tajawal_regular, FontWeight.Normal, "regular"),
                Triple(R.font.tajawal_medium, FontWeight.Medium, "medium"),
                Triple(R.font.tajawal_medium, FontWeight.SemiBold, "semi-bold"),
                Triple(R.font.tajawal_bold, FontWeight.Bold, "bold"),
                Triple(R.font.tajawal_bold, FontWeight.ExtraBold, "extra-bold")
            )

            // محاولة تحميل كل وزن
            for ((resId, weight, name) in fontConfigs) {
                val font = safeTryLoadFont(resId, weight, name)
                if (font != null) {
                    fonts.add(font)
                    successCount++
                }
            }

            // إذا نجح تحميل واحد على الأقل، أنشئ عائلة الخطوط
            if (successCount > 0) {
                Log.i(TAG, "✅ FontFamily created successfully with $successCount/${fontConfigs.size} fonts loaded")
                isInitialized = true
                loadingError = null
                return FontFamily(fonts)
            } else {
                // إذا فشل الكل، استخدم الخط الافتراضي
                Log.w(TAG, "⚠️ Failed to load ANY fonts - using system default as fallback")
                loadingError = "All fonts failed to load - using system default"
                isInitialized = true
                return FontFamily.Default
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Exception in buildSafeFontFamily", e)
            loadingError = "Exception: ${e.message}"
            isInitialized = true
            return FontFamily.Default
        }
    }

    /**
     * الحصول على آخر رسالة خطأ
     */
    fun getLastError(): String? = loadingError

    /**
     * إعادة تعيين الأخطاء
     */
    fun clearError() {
        loadingError = null
    }

    /**
     * فحص إذا كان الخط يعمل بشكل صحيح
     */
    fun isFontLoaded(fontFamily: FontFamily): Boolean {
        return isInitialized && fontFamily != FontFamily.Default
    }
    
    /**
     * حالة الخط الحالية (للتشخيص)
     */
    fun getStatus(): String {
        return buildString {
            appendLine("FontLoader Status:")
            appendLine("- Initialized: $isInitialized")
            appendLine("- Last Error: ${loadingError ?: "None"})")
        }
    }
}
