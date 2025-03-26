package org.whitedevil.igrs.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import org.whitedevil.igrs.ui.common.LocalDarkTheme
import com.kyant.monet.TonalPalettes
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import io.material.hct.Hct

@Composable
fun Number.autoDark(isDarkTheme: Boolean = LocalDarkTheme.current.isDarkTheme()): Double =
    if (!isDarkTheme) this.toDouble()
    else
        when (this.toDouble()) {
            6.0 -> 98.0
            10.0 -> 99.0
            20.0 -> 95.0
            25.0 -> 90.0
            30.0 -> 90.0
            40.0 -> 80.0
            50.0 -> 60.0
            60.0 -> 50.0
            70.0 -> 40.0
            80.0 -> 40.0
            90.0 -> 30.0
            95.0 -> 20.0
            98.0 -> 10.0
            99.0 -> 10.0
            100.0 -> 20.0
            else -> this.toDouble()
        }

@Immutable
data class FixedColorRoles(
    val primaryFixed: Color,
    val primaryFixedDim: Color,
    val onPrimaryFixed: Color,
    val onPrimaryFixedVariant: Color,
    val secondaryFixed: Color,
    val secondaryFixedDim: Color,
    val onSecondaryFixed: Color,
    val onSecondaryFixedVariant: Color,
    val tertiaryFixed: Color,
    val tertiaryFixedDim: Color,
    val onTertiaryFixed: Color,
    val onTertiaryFixedVariant: Color,
) {
    companion object {
        internal val unspecified =
            FixedColorRoles(
                primaryFixed = Color.Unspecified,
                primaryFixedDim = Color.Unspecified,
                onPrimaryFixed = Color.Unspecified,
                onPrimaryFixedVariant = Color.Unspecified,
                secondaryFixed = Color.Unspecified,
                secondaryFixedDim = Color.Unspecified,
                onSecondaryFixed = Color.Unspecified,
                onSecondaryFixedVariant = Color.Unspecified,
                tertiaryFixed = Color.Unspecified,
                tertiaryFixedDim = Color.Unspecified,
                onTertiaryFixed = Color.Unspecified,
                onTertiaryFixedVariant = Color.Unspecified,
            )

        @Stable
        internal fun fromTonalPalettes(palettes: TonalPalettes): FixedColorRoles {
            return with(palettes) {
                FixedColorRoles(
                    primaryFixed = accent1(90.toDouble()),
                    primaryFixedDim = accent1(80.toDouble()),
                    onPrimaryFixed = accent1(10.toDouble()),
                    onPrimaryFixedVariant = accent1(30.toDouble()),
                    secondaryFixed = accent2(90.toDouble()),
                    secondaryFixedDim = accent2(80.toDouble()),
                    onSecondaryFixed = accent2(10.toDouble()),
                    onSecondaryFixedVariant = accent2(30.toDouble()),
                    tertiaryFixed = accent3(90.toDouble()),
                    tertiaryFixedDim = accent3(80.toDouble()),
                    onTertiaryFixed = accent3(10.toDouble()),
                    onTertiaryFixedVariant = accent3(30.toDouble()),
                )
            }
        }

        @Stable
        internal fun fromColorSchemes(
            lightColors: ColorScheme,
            darkColors: ColorScheme,
        ): FixedColorRoles {
            return FixedColorRoles(
                primaryFixed = lightColors.primaryContainer,
                onPrimaryFixed = lightColors.onPrimaryContainer,
                onPrimaryFixedVariant = darkColors.primaryContainer,
                secondaryFixed = lightColors.secondaryContainer,
                onSecondaryFixed = lightColors.onSecondaryContainer,
                onSecondaryFixedVariant = darkColors.secondaryContainer,
                tertiaryFixed = lightColors.tertiaryContainer,
                onTertiaryFixed = lightColors.onTertiaryContainer,
                onTertiaryFixedVariant = darkColors.tertiaryContainer,
                primaryFixedDim = darkColors.primary,
                secondaryFixedDim = darkColors.secondary,
                tertiaryFixedDim = darkColors.tertiary,
            )
        }
    }
}

const val DEFAULT_SEED_COLOR = 0xa3d48d

@Composable
@ReadOnlyComposable
fun Int.generateLabelColor(): Color =
    Color(Hct.from(hue = (this % 360).toDouble(), chroma = 36.0, tone = 80.0).toInt())
        .harmonizeWithPrimary()

@Composable
@ReadOnlyComposable
fun Int.generateOnLabelColor(): Color =
    Color(Hct.from(hue = (this % 360).toDouble(), chroma = 36.0, tone = 20.0).toInt())
        .harmonizeWithPrimary()

val ErrorTonalPalettes = Color.Red.toTonalPalettes()