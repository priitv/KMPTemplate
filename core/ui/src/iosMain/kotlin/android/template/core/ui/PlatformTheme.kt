package android.template.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun resolveColorScheme(darkTheme: Boolean): ColorScheme =
    if (darkTheme) DarkColorScheme else LightColorScheme

@Composable
internal actual fun PlatformThemeEffect(colorScheme: ColorScheme, darkTheme: Boolean) {
    // iOS handles status bar appearance natively via UIStatusBarStyle in Info.plist
}
