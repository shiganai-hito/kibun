package com.example.kibun.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD4E2D4),
    secondary = Color(0xFFFFD5CD),
    tertiary = Color(0xFFFAE3D9),
    background = Color(0xFF1A1C1E),
    surface = Color(0xFF2D2F31),
    onPrimary = Color(0xFF1A1C1E),
    onSecondary = Color(0xFF1A1C1E),
    onTertiary = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E2),
    onSurface = Color(0xFFE2E2E2),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF8E9775), // オリーブ・セージ系
    secondary = Color(0xFFE28E8E), // くすみピンク
    tertiary = Color(0xFFEADBC8), // ベージュ
    background = Color(0xFFF9F7F7), // オフホワイト
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF454545),
    onBackground = Color(0xFF454545),
    onSurface = Color(0xFF454545),
)

@Composable
fun KibunAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val context = view.context
            var currentContext = context
            while (currentContext is android.content.ContextWrapper) {
                if (currentContext is Activity) break
                currentContext = currentContext.baseContext
            }
            
            val window = (currentContext as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
