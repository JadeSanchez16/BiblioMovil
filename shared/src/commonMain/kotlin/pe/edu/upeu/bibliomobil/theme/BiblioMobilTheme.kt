package pe.edu.upeu.bibliomobil.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0E4D78),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2E9F7),
    onPrimaryContainer = Color(0xFF082F49),
    secondary = Color(0xFF8A5A12),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDEA6),
    onSecondaryContainer = Color(0xFF2D1700),
    tertiary = Color(0xFF4D6759),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD0E8D9),
    onTertiaryContainer = Color(0xFF0A2116),
    background = Color(0xFFF7FAFC),
    onBackground = Color(0xFF17212B),
    surface = Color(0xFFF7FAFC),
    onSurface = Color(0xFF17212B),
    surfaceVariant = Color(0xFFDCE4EA),
    onSurfaceVariant = Color(0xFF404950),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF0F5F8),
    surfaceContainer = Color(0xFFEAF0F4),
    surfaceContainerHigh = Color(0xFFE3EAEE),
    surfaceContainerHighest = Color(0xFFDDE4E9),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF6F7A82),
    outlineVariant = Color(0xFFC0C8CF),
    inverseSurface = Color(0xFF2B3137),
    inverseOnSurface = Color(0xFFEEF1F4),
    inversePrimary = Color(0xFF9DCEF0),
    scrim = Color(0xFF000000)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9DCEF0),
    onPrimary = Color(0xFF05324F),
    primaryContainer = Color(0xFF0B3D60),
    onPrimaryContainer = Color(0xFFD2E9F7),
    secondary = Color(0xFFF2C16E),
    onSecondary = Color(0xFF4A2A00),
    secondaryContainer = Color(0xFF684000),
    onSecondaryContainer = Color(0xFFFFDEA6),
    tertiary = Color(0xFFB4CCBD),
    onTertiary = Color(0xFF20362A),
    tertiaryContainer = Color(0xFF364D3F),
    onTertiaryContainer = Color(0xFFD0E8D9),
    background = Color(0xFF11171C),
    onBackground = Color(0xFFE1E7EC),
    surface = Color(0xFF11171C),
    onSurface = Color(0xFFE1E7EC),
    surfaceVariant = Color(0xFF404950),
    onSurfaceVariant = Color(0xFFC0C8CF),
    surfaceContainerLowest = Color(0xFF090D10),
    surfaceContainerLow = Color(0xFF171D22),
    surfaceContainer = Color(0xFF1B2228),
    surfaceContainerHigh = Color(0xFF252C32),
    surfaceContainerHighest = Color(0xFF30373D),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF89939B),
    outlineVariant = Color(0xFF404950),
    inverseSurface = Color(0xFFE1E7EC),
    inverseOnSurface = Color(0xFF2E343A),
    inversePrimary = Color(0xFF0E4D78),
    scrim = Color(0xFF000000)
)

@Composable
fun BiblioMobilTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
