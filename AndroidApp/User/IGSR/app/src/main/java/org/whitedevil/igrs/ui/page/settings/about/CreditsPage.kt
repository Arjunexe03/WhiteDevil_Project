package org.whitedevil.igrs.ui.page.settings.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import org.whitedevil.igrs.R
import org.whitedevil.igrs.ui.component.BackButton
import org.whitedevil.igrs.ui.component.CreditItem
import org.whitedevil.igrs.ui.items.Credit


const val APACHE_V2 = "Apache License 2.0"
const val BSD = "BSD 3-Clause \"New\" or \"Revised\" License"

const val GOOGLE = "Google"
const val AOSP = "The Android Open Source Project"
const val ASDKL = "Android Software Development Kit License"
const val JETBRAINS = "JetBrains Team"
const val TENCENT = "Tencent Wechat, Inc."
const val SQUARE = "Square, Inc."

const val firebase = "https://github.com/firebase/firebase-android-sdk"
const val navigation = "https://developer.android.com/develop/ui/compose/navigation"
const val splash = "https://developer.android.com/develop/ui/views/launch/splash-screen"
const val materialIcon = "https://fonts.google.com/icons"
const val monet = "https://github.com/Kyant0/Monet"
const val jetpack = "https://github.com/androidx/androidx"
const val coil = "https://github.com/coil-kt/coil"
const val mmkv = "https://github.com/Tencent/MMKV"
const val kotlin = "https://kotlinlang.org/"
const val okhttp = "https://github.com/square/okhttp"
const val accompanist = "https://github.com/google/accompanist"
const val material3 = "https://m3.material.io/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsPage(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    val creditsList = remember {
        listOf(
            Credit("Accompanist", GOOGLE, APACHE_V2, "0.34.0", accompanist),
            Credit("Android Jetpack", AOSP, APACHE_V2, "2024.09.03", jetpack),
            Credit("Coil", "Coil Contributors", APACHE_V2, "2.5.0", coil),
            Credit("Firebase SDK-ktx", GOOGLE, APACHE_V2, "33.4.0", firebase),
            Credit("Compose Navigation", AOSP, APACHE_V2, "2.8.2", navigation),
            Credit("Kotlin", JETBRAINS, APACHE_V2, "2.0.20", kotlin),
            Credit("MMKV", TENCENT, BSD, "1.3.9", mmkv),
            Credit("okhttp", SQUARE, APACHE_V2, "5.0.0-alpha.10", okhttp),
            Credit("SplashScreen", AOSP, APACHE_V2, "1.0.1", splash),
            Credit("Material Design 3", AOSP, APACHE_V2, "1.12.0", material3),
            Credit("Material Icons", AOSP, APACHE_V2, "", materialIcon),
        )
    }

    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.credits),
                    )
                }, navigationIcon = {
                    BackButton {
                        onNavigateBack()
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(creditsList) { item ->
                    CreditItem(
                        credit = item,
                    ) {
                        uriHandler.openUri(item.url)
                    }
                }
            }
        }
    )
}