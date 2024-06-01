package com.mdev.chatapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.StringAndDateConvertor
import com.mdev.chatapp.util.UIEvent
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel,
    navDrawerViewModel: NavigateDrawerViewModel
) {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.ChatScreen
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val state = profileViewModel.state

    LaunchedEffect(navDrawerViewModel, context) {
        navDrawerViewModel.uiEvent.collect {
            when (it) {
                is UIEvent.Logout -> {
                    onLogout(Route.AuthNavigator)
                }

                is UIEvent.NavigateTo -> {
                    onNavigateTo(it.route)
                }

                is UIEvent.Back -> {
                    onBackClick()
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        BaseScreen(
            scope = scope,
            drawerState = drawerState,
            selectedItem = selectedItem,
            navDrawerViewModel = navDrawerViewModel,
            scrollBehavior = scrollBehavior,
            content = {
                ProfileContent(
                    state = state,
                    onValueChange = { profileViewModel.onEvent(it) }
                )
            }
        )
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ProfileContent(
    state: ProfileState,
    onValueChange: (ProfileUIEvent) -> Unit,
) {

    val dateDialogState = rememberMaterialDialogState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 8.dp
            ),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .padding(35.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    CircularImage()
                    Spacer(modifier = Modifier.size(8.dp))
                    MarkdownText(markdown = "**@${state.userName}**")
                }
            }
        }
        item {
            MarkdownText(markdown = "**${stringResource(id = R.string.info)}**")
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            OutlinedTextField(
                value = state.name,
                onValueChange = { onValueChange(ProfileUIEvent.OnNameChanged(it)) },
                enabled = state.isUpdating,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.name)) }
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField  (
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                dateDialogState.show()
                            },
                            enabled = state.isUpdating
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar"
                            )
                        }
                    },
                    value = StringAndDateConvertor.dateToString(state.dateOfBirth),
                    onValueChange = { },
                    enabled = state.isUpdating,
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.birthday)) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField (
                    value = state.age.toString(),
                    onValueChange = {  },
                    enabled = false,
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.age)) },

                )
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            MarkdownText(markdown = "**${stringResource(id = R.string.body_mass_index)}**")
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            BMIDropdownMenu(
                state = state,
                onUIEvent = onValueChange
            )
        }
        item{ Spacer(modifier = Modifier.height(16.dp)) }
        item {
            OutlinedTextField(
                value = state.bmi.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.BMI)) }
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            if (state.isUpdating) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 2.dp, end = 2.dp, bottom = 8.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .height(32.dp)
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                        ) {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            onClick = {
                                onValueChange(ProfileUIEvent.OnSaveClicked)
                            },
                        ) {
                            MarkdownText(markdown = "**${stringResource(id = R.string.confirm)}**")
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp, start = 2.dp, end = 2.dp, bottom = 8.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .height(32.dp)
                            .background(MaterialTheme.colorScheme.inverseOnSurface),
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            onClick = {
                                onValueChange(ProfileUIEvent.OnCancelClicked)
                            },
                        ) {
                            MarkdownText(markdown = "**${stringResource(id = R.string.cancel)}**")
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 2.dp, end = 2.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.inverseOnSurface),
                    contentAlignment = Alignment.Center,
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        onClick = {
                            onValueChange(ProfileUIEvent.OnEditClicked)
                        },
                    ) {
                        MarkdownText(markdown = "**${stringResource(id = R.string.edit)}**")
                    }
                }
            }
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = stringResource(id = R.string.accept))
            negativeButton(text = stringResource(id = R.string.cancel))
        }
    ) {
        datepicker(
            initialDate = state.dateOfBirth,
            title = stringResource(id = R.string.select_date),
            allowedDateValidator = {
                it.isAfter(LocalDate.now().minusYears(100)) && it.isBefore(LocalDate.now())
            }
        ) { selectedDate ->
            onValueChange(ProfileUIEvent.OnDateOfBirthChanged(selectedDate))
        }
    }
}


@Preview
@Composable
fun CircularImage(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    placeholder: Painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = imageUrl)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                error(R.drawable.tao_la_vua)
            }).build()
    ),
    contentDescription: String = "Profile Image"
) {
    Image(
        painter = placeholder,
        contentDescription = contentDescription,
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIDropdownMenu(
    state: ProfileState,
    onUIEvent: (ProfileUIEvent) -> Unit
) {
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }

    val height = (0..250).map { it.toString() }
    val weight = (0..150).map { it.toString() }

    Row (
        modifier = Modifier
            .fillMaxSize()
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = expanded1,
            onExpandedChange = { expanded1 = !expanded1 }
        ) {
            OutlinedTextField(
                enabled = state.isUpdating,
                value = state.height.toString() + "cm",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1) },
                modifier = Modifier
                    .menuAnchor(),
                label = { Text(stringResource(id = R.string.height)) }
            )
            ExposedDropdownMenu(
                expanded = expanded1,
                onDismissRequest = { expanded1 = false }
            ) {
                height.forEach { selectionOption ->
                    DropdownMenuItem(
                        enabled = state.isUpdating,
                        text = { Text(selectionOption) },
                        onClick = {
                            onUIEvent(ProfileUIEvent.OnHeightChanged(selectionOption.toFloat()))
                            expanded1 = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = expanded2,
            onExpandedChange = { expanded2 = !expanded2 },
        ) {
            OutlinedTextField(
                value = state.weight.toString() + "kg",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) },
                modifier = Modifier
                    .menuAnchor()
                    .weight(1f),
                label = { Text(stringResource(id = R.string.weight)) },
                enabled = state.isUpdating
            )
            ExposedDropdownMenu(
                expanded = expanded2,
                onDismissRequest = { expanded2 = false },
            ) {
                weight.forEach { selectionOption ->
                    DropdownMenuItem(
                        enabled = state.isUpdating,
                        text = { Text(selectionOption) },
                        onClick = {
                            onUIEvent(ProfileUIEvent.OnWeightChanged(selectionOption.toFloat()))
                            expanded2 = false
                        }
                    )
                }
            }
        }
    }
}
