package org.whitedevil.igrs.ui.page.home

import android.widget.Toast
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.database
import org.whitedevil.igrs.App.Companion.context
import org.whitedevil.igrs.R
import org.whitedevil.igrs.ui.common.HapticFeedback.slightHapticFeedback
import org.whitedevil.igrs.ui.common.LocalWindowWidthState
import org.whitedevil.igrs.ui.component.AdjacentLabel
import org.whitedevil.igrs.ui.items.Problem
import org.whitedevil.igrs.util.FirebaseRepository

private const val HeaderSpacingDp = 28

@Composable
fun HomePage(
    onMenuOpen: (() -> Unit) = {},
) {
    val view = LocalView.current
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            val expanded = LocalWindowWidthState.current != WindowWidthSizeClass.Compact
            val repository = remember { FirebaseRepository() }
            Column(modifier = Modifier.padding(6.dp), horizontalAlignment = Alignment.End) {
                val post: () -> Unit = {
                    val newProblem = Problem(name, phoneNumber, address, problem)
                    repository.addProblem(newProblem) { success ->
                        name = ""
                        phoneNumber = ""
                        address = ""
                        problem = ""
                        view.slightHapticFeedback()
                        if (success) {
                            Toast.makeText(context, "Problem submitted successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to submit problem!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                FloatingActionButton(
                    onClick = post,
                    content = {
                        if (expanded) {
                            Row(
                                modifier = Modifier.widthIn(min = 80.dp).padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(Icons.Outlined.Report, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Text(stringResource(R.string.report))
                            }
                        } else {
                            Icon(
                                Icons.Outlined.Report,
                                contentDescription = stringResource(R.string.report),
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
                               },
    ) { windowInsetsPadding ->
        val windowWidthSizeClass = LocalWindowWidthState.current
        val spacerHeight =
            with(LocalDensity.current) {
                if (windowWidthSizeClass != WindowWidthSizeClass.Compact) 0f
                else HeaderSpacingDp.dp.toPx()
            }
        var headerOffset by remember { mutableFloatStateOf(spacerHeight) }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .then(
                        if (windowWidthSizeClass != WindowWidthSizeClass.Compact) Modifier
                        else Modifier.nestedScroll(
                            connection = TopBarNestedScrollConnection(
                                maxOffset = spacerHeight,
                                flingAnimationSpec = rememberSplineBasedDecay(),
                                offset = { headerOffset },
                                onOffsetUpdate = { headerOffset = it },
                            )
                        )
                    )
        ) {
            CompositionLocalProvider(LocalOverscrollFactory provides null) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.height(with(LocalDensity.current) { headerOffset.toDp() }))
                    Header(onMenuOpen = onMenuOpen, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.padding(windowInsetsPadding)
                    ) {
                        item {
                            val description = stringResource(R.string.name)

                            Column(Modifier.padding(horizontal = 24.dp)) {
                                AdjacentLabel(text = description, modifier = Modifier.padding(top = 12.dp))
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                    value = name,
                                    onValueChange = { name = it },
                                    keyboardActions = KeyboardActions.Default,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }
                        item {
                            val description = stringResource(R.string.phone_number)

                            Column(Modifier.padding(horizontal = 24.dp)) {
                                AdjacentLabel(text = description, modifier = Modifier.padding(top = 12.dp))
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    keyboardActions = KeyboardActions.Default,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }
                        item {
                            val description = stringResource(R.string.address)

                            Column(Modifier.padding(horizontal = 24.dp)) {
                                AdjacentLabel(text = description, modifier = Modifier.padding(top = 12.dp))
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                    value = address,
                                    onValueChange = { address = it },
                                    keyboardActions = KeyboardActions.Default,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    shape = RoundedCornerShape(16.dp),
                                    minLines = 3,
                                )
                            }
                        }
                        item {
                            val description = stringResource(R.string.problem)

                            Column(Modifier.padding(horizontal = 24.dp)) {
                                AdjacentLabel(text = description, modifier = Modifier.padding(top = 12.dp))
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                    value = problem,
                                    onValueChange = { problem = it },
                                    keyboardActions = KeyboardActions.Default,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    shape = RoundedCornerShape(16.dp),
                                    minLines = 6,
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun Header(modifier: Modifier = Modifier, onMenuOpen: () -> Unit = {}) {
    val windowWidthSizeClass = LocalWindowWidthState.current
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            HeaderExpanded(modifier = modifier)
        }
        else -> {
            HeaderCompact(modifier = modifier, onMenuOpen = onMenuOpen)
        }
    }
}

@Composable
private fun HeaderCompact(modifier: Modifier = Modifier, onMenuOpen: () -> Unit) {

    Row(modifier = modifier.height(64.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onMenuOpen, modifier = Modifier) {
            Icon(
                imageVector = Icons.Outlined.Menu,
                contentDescription = stringResource(R.string.show_navigation_drawer),
                modifier = Modifier,
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            stringResource(R.string.report),
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                ),
        )
    }
}

@Composable
private fun HeaderExpanded(modifier: Modifier = Modifier) {
    Row(modifier = modifier.height(64.dp), verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            stringResource(R.string.report),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
        )
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
fun FAB(
    modifier: Modifier = Modifier,
    name: String,
    phoneNumber: String,
    address: String,
    problem: String,
) {

}