package android.template.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal expect fun PlatformThemeEffect(colorScheme: ColorScheme, darkTheme: Boolean)

@Composable
internal expect fun resolveColorScheme(darkTheme: Boolean): ColorScheme
