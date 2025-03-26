package org.whitedevil.igrs.util

import android.os.Build
import androidx.annotation.DeprecatedSinceApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.android.material.color.DynamicColors
import org.whitedevil.igrs.App
import org.whitedevil.igrs.App.Companion.applicationScope
import org.whitedevil.igrs.App.Companion.isDebugBuild
import org.whitedevil.igrs.ui.theme.DEFAULT_SEED_COLOR
import com.kyant.monet.PaletteStyle
import com.tencent.mmkv.MMKV
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

const val DEBUG = "debug"
const val DARK_THEME_VALUE = "dark_theme_value"
const val WELCOME_DIALOG = "welcome_dialog"
private const val LANGUAGE = "language"
const val NOTIFICATION = "notification"
private const val THEME_COLOR = "theme_color"
const val PALETTE_STYLE = "palette_style"
const val AUTO_UPDATE = "auto_update"
const val UPDATE_CHANNEL = "update_channel"
private const val DYNAMIC_COLOR = "dynamic_color"
const val CELLULAR_DOWNLOAD = "cellular_download"
private const val HIGH_CONTRAST = "high_contrast"
const val NOT_SPECIFIED = 0
const val DEFAULT = NOT_SPECIFIED
const val SYSTEM_DEFAULT = NOT_SPECIFIED
const val STABLE = 0
const val PRE_RELEASE = 1

val paletteStyles =
    listOf(
        PaletteStyle.TonalSpot,
        PaletteStyle.Spritz,
        PaletteStyle.FruitSalad,
        PaletteStyle.Vibrant,
        PaletteStyle.Monochrome,
    )

const val STYLE_TONAL_SPOT = 0
const val STYLE_SPRITZ = 1
const val STYLE_FRUIT_SALAD = 2
const val STYLE_VIBRANT = 3
const val STYLE_MONOCHROME = 4
const val TEST = "test"
const val TEST2 = "test"

private val StringPreferenceDefaults =
    mapOf(
        TEST to "default",
    )

private val BooleanPreferenceDefaults =
    mapOf(
        NOTIFICATION to true,
    )

private val IntPreferenceDefaults =
    mapOf(
        LANGUAGE to SYSTEM_DEFAULT,
        PALETTE_STYLE to 0,
        DARK_THEME_VALUE to DarkThemePreference.FOLLOW_SYSTEM,
        WELCOME_DIALOG to 1,
        UPDATE_CHANNEL to STABLE,
    )

private val LongPreferenceDefaults = mapOf(TEST2 to 1L)

fun String.getStringDefault() = StringPreferenceDefaults.getOrElse(this) { "" }

object PreferenceUtil {
    private val kv: MMKV = MMKV.defaultMMKV()
    private val json = Json {
        ignoreUnknownKeys = true
        allowStructuredMapKeys = true
    }

    fun String.getInt(default: Int = IntPreferenceDefaults.getOrElse(this) { 0 }): Int =
        kv.decodeInt(this, default)

    fun String.getString(
        default: String = StringPreferenceDefaults.getOrElse(this) { "" }
    ): String = kv.decodeString(this) ?: default

    fun String.getBoolean(
        default: Boolean = BooleanPreferenceDefaults.getOrElse(this) { false }
    ): Boolean = kv.decodeBool(this, default)

    fun String.getLong(default: Long = LongPreferenceDefaults.getOrElse(this) { 0L }) =
        kv.decodeLong(this, default)

    fun String.updateString(newString: String) = kv.encode(this, newString)

    fun String.updateInt(newInt: Int) = kv.encode(this, newInt)

    fun String.updateLong(newLong: Long) = kv.encode(this, newLong)

    fun String.updateBoolean(newValue: Boolean) = kv.encode(this, newValue)

    fun updateValue(key: String, b: Boolean) = key.updateBoolean(b)

    fun encodeInt(key: String, int: Int) = key.updateInt(int)

    fun encodeString(key: String, string: String) = key.updateString(string)

    fun containsKey(key: String) = kv.containsKey(key)

    fun isNetworkAvailableForDownload() =
        CELLULAR_DOWNLOAD.getBoolean() || !App.connectivityManager.isActiveNetworkMetered

    fun isAutoUpdateEnabled(): Boolean {
        return when {
            isDebugBuild() -> false
            else -> AUTO_UPDATE.getBoolean()
        }
    }

    @DeprecatedSinceApi(api = 33)
    fun getLocaleFromPreference(): Locale? {
        val languageCode = LANGUAGE.getInt()
        return LocaleLanguageCodeMap.entries.find { it.value == languageCode }?.key
    }

    fun saveLocalePreference(locale: Locale?) {
        if (Build.VERSION.SDK_INT >= 33) {
            // No op
        } else {
            LANGUAGE.updateInt(LocaleLanguageCodeMap[locale] ?: SYSTEM_DEFAULT)
        }
    }


    data class AppSettings(
        val darkTheme: DarkThemePreference = DarkThemePreference(),
        val isDynamicColorEnabled: Boolean = false,
        val seedColor: Int = DEFAULT_SEED_COLOR,
        val paletteStyleIndex: Int = 0,
    )

    private val mutableAppSettingsStateFlow =
        MutableStateFlow(
            AppSettings(
                DarkThemePreference(
                    darkThemeValue =
                        kv.decodeInt(DARK_THEME_VALUE, DarkThemePreference.FOLLOW_SYSTEM),
                    isHighContrastModeEnabled = kv.decodeBool(HIGH_CONTRAST, false),
                ),
                isDynamicColorEnabled =
                    kv.decodeBool(DYNAMIC_COLOR, DynamicColors.isDynamicColorAvailable()),
                seedColor = kv.decodeInt(THEME_COLOR, DEFAULT_SEED_COLOR),
                paletteStyleIndex = kv.decodeInt(PALETTE_STYLE, 0),
            )
        )
    val AppSettingsStateFlow = mutableAppSettingsStateFlow.asStateFlow()

    fun modifyDarkThemePreference(
        darkThemeValue: Int = AppSettingsStateFlow.value.darkTheme.darkThemeValue,
        isHighContrastModeEnabled: Boolean =
            AppSettingsStateFlow.value.darkTheme.isHighContrastModeEnabled,
    ) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update {
                it.copy(
                    darkTheme =
                        AppSettingsStateFlow.value.darkTheme.copy(
                            darkThemeValue = darkThemeValue,
                            isHighContrastModeEnabled = isHighContrastModeEnabled,
                        )
                )
            }
            kv.encode(DARK_THEME_VALUE, darkThemeValue)
            kv.encode(HIGH_CONTRAST, isHighContrastModeEnabled)
        }
    }

    fun modifyThemeSeedColor(colorArgb: Int, paletteStyleIndex: Int) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update {
                it.copy(seedColor = colorArgb, paletteStyleIndex = paletteStyleIndex)
            }
            kv.encode(THEME_COLOR, colorArgb)
            kv.encode(PALETTE_STYLE, paletteStyleIndex)
        }
    }

    fun switchDynamicColor(
        enabled: Boolean = !mutableAppSettingsStateFlow.value.isDynamicColorEnabled
    ) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update { it.copy(isDynamicColorEnabled = enabled) }
            kv.encode(DYNAMIC_COLOR, enabled)
        }
    }


    private const val TAG = "PreferenceUtil"
}

data class DarkThemePreference(
    val darkThemeValue: Int = FOLLOW_SYSTEM,
    val isHighContrastModeEnabled: Boolean = false,
) {
    companion object {
        const val FOLLOW_SYSTEM = 1
        const val ON = 2
        const val OFF = 3
    }

    @Composable
    fun isDarkTheme(): Boolean {
        return if (darkThemeValue == FOLLOW_SYSTEM) isSystemInDarkTheme() else darkThemeValue == ON
    }

    @Composable
    fun getDarkThemeDesc(): String {
        return when (darkThemeValue) {
            FOLLOW_SYSTEM -> stringResource(R.string.follow_system)
            ON -> stringResource(R.string.on)
            else -> stringResource(R.string.off)
        }
    }
}