package org.whitedevil.igrs.ui.page.settings.about

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.TipsAndUpdates
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.whitedevil.igrs.BuildConfig
//import org.whitedevil.igrs.BuildConfig
import org.whitedevil.igrs.R
import org.whitedevil.igrs.ui.common.HapticFeedback.slightHapticFeedback
import org.whitedevil.igrs.ui.common.Route
import org.whitedevil.igrs.ui.component.BackButton
import org.whitedevil.igrs.ui.component.CurlyCornerShape
//import org.whitedevil.igrs.ui.theme.customFonts

const val release = "https://www.instagram.com/jaano_bharat/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(
    onNavigateBack: () -> Unit, onNavigateTo: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val view = LocalView.current
    var clickTime by remember { mutableLongStateOf(System.currentTimeMillis() - 2000) }
    var pressAMP by remember { mutableFloatStateOf(16f) }
    val animatedPress by animateFloatAsState(
        targetValue = pressAMP, animationSpec = tween(), label = ""
    )

    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(title = {}, navigationIcon = { BackButton(onNavigateBack) }, actions = {
            IconButton(
                modifier = Modifier, onClick = {
                    view.slightHapticFeedback()
                    onNavigateTo(Route.CREDITS)
                }) {
                Icon(
                    imageVector = Icons.Rounded.Balance,
                    contentDescription = stringResource(R.string.open_source_licenses),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        })
    }, content = {
        Column {
            Spacer(modifier = Modifier.height(it.calculateTopPadding()))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                item {
                    Column(
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                pressAMP = 0f
                                tryAwaitRelease()
                                pressAMP = 16f
                            }, onTap = {
                                if (System.currentTimeMillis() - clickTime > 2000) {
                                    clickTime = System.currentTimeMillis()
                                    uriHandler.openUri(release)
                                } else {
                                    clickTime = System.currentTimeMillis()
                                }
                            })
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = CurlyCornerShape(amp = animatedPress.toDouble()),
                                )
                                .shadow(
                                    elevation = 10.dp,
                                    shape = CurlyCornerShape(amp = animatedPress.toDouble()),
                                    ambientColor = MaterialTheme.colorScheme.primaryContainer,
                                    spotColor = MaterialTheme.colorScheme.primaryContainer,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                modifier = Modifier.size(100.dp),
                                imageVector = Icons.Rounded.Info,
                                contentDescription = stringResource(R.string.app_name),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                        }
                        Spacer(modifier = Modifier.height(48.dp))
                        BadgedBox(badge = {
                            Badge(
                                modifier = Modifier.animateContentSize(tween(800)),
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.tertiary,
                            ) {
                                Text("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                            }
                        }) {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.displaySmall,
//                                fontFamily = customFonts
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    })
}

@Immutable
sealed class RoundIconButtonType(
    val iconVector: ImageVector? = null,
    val descString: String? = null,
    open val size: Dp = 24.dp,
    open val offset: Modifier = Modifier.offset(),
    open val backgroundColor: Color = Color.Unspecified,
    open val onClick: () -> Unit = {},
) {

    @Immutable
    data class Developer(
        val desc: String = "Developer",
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconVector = Icons.Rounded.Code,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )

    @Immutable
    data class Update(
        val desc: String = "Update",
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconVector = Icons.Rounded.Update,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )


    @Immutable
    data class Terms(
        val desc: String = "Terms",
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconVector = Icons.Rounded.Description,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )

    @Immutable
    data class Tips(
        val desc: String = "Tips",
        override val offset: Modifier = Modifier.offset(x = (3).dp),
        override val backgroundColor: Color,
        override val onClick: () -> Unit = {},
    ) : RoundIconButtonType(
        iconVector = Icons.Rounded.TipsAndUpdates,
        descString = desc,
        backgroundColor = backgroundColor,
        onClick = onClick,
    )
}

@Composable
private fun RoundIconButton(type: RoundIconButtonType) {
    IconButton(
        modifier = Modifier
            .size(70.dp)
            .background(
                color = type.backgroundColor,
                shape = CircleShape,
            ), onClick = { type.onClick() }) {
        when (type) {

            is RoundIconButtonType.Developer -> {
                Icon(
                    modifier = Modifier,
                    imageVector = type.iconVector!!,
                    contentDescription = type.descString,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            is RoundIconButtonType.Tips -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    imageVector = type.iconVector!!,
                    contentDescription = type.descString,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            is RoundIconButtonType.Terms -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    imageVector = type.iconVector!!,
                    contentDescription = type.descString,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            is RoundIconButtonType.Update -> {
                Icon(
                    modifier = type.offset.size(type.size),
                    imageVector = type.iconVector!!,
                    contentDescription = type.descString,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
