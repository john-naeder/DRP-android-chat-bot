package com.mdev.chatapp.ui.settings

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mdev.chatapp.ui.common.nav_drawer.NavigateDrawerViewModel
import com.mdev.chatapp.ui.navgraph.Route
import com.mdev.chatapp.util.UIEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.option.OptionDialog
import com.maxkeppeler.sheets.option.models.DisplayMode
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig
import com.maxkeppeler.sheets.option.models.OptionSelection
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.common.BaseScreen
import com.murgupluoglu.flagkit.FlagKit
import androidx.compose.material3.Text as Text1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navDrawerViewModel: NavigateDrawerViewModel,
    onBackClick: () -> Unit,
    onLogout: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onSwitchTheme: () -> Unit,
    settingViewmodel: SettingViewModel

){
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.SettingsScreen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    val state = settingViewmodel.state

    BaseScreen(
        scope = scope,
        drawerState = drawerState,
        selectedItem = selectedItem,
        navDrawerViewModel = navDrawerViewModel,
        scrollBehavior = scrollBehavior,
        content = {
            Column {
                SettingContent(
                    state = state,
                    onSwitchTheme = {
                        settingViewmodel.onEvent(SettingUIEvent.OnThemeChanged)
                        onSwitchTheme()
                    },
                    onSwitchLanguage = {
                        settingViewmodel.onEvent(SettingUIEvent.OnLanguageChanged(it))
                    }
                )
            }

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    state: SettingState,
    onSwitchTheme: () -> Unit,
    onSwitchLanguage: (String) -> Unit
){
    val languageSettingState =  rememberUseCaseState()
    val context = LocalContext.current
    val englishText = stringResource(id = R.string.en)
    val vietnameseText = stringResource(id = R.string.vi)

    val options = listOf(
        Option(
            IconSource(FlagKit.getResId(context, "US")),
            titleText = englishText,
            disabled = state.language == "en",
            subtitleText = "Tiếng Anh",
        ),
        Option(
            IconSource(FlagKit.getResId(context, "VN")),
            titleText = vietnameseText,
            selected = state.language == "vi",
            subtitleText= "Vietnamese",
        ),
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Text1(
                text = stringResource(id =  R.string.theme_settings),
                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            ThemeSwitcher(
                darkTheme = state.isDarkTheme,
                size = 35.dp,
                onClick = onSwitchTheme
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text1(
                text = stringResource(id = R.string.language_settings),
                style = MaterialTheme.typography.titleLarge
            )
//            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .width(70.dp)
                    .height(35.dp),
                onClick = { languageSettingState.show() }
            ) {
                Icon(imageVector = Icons.Default.Language, contentDescription = "Language")
            }
        }
    }

    OptionDialog(
        state = languageSettingState,
        selection = OptionSelection.Single(options) { index, _ ->
            when(index){
                0 -> onSwitchLanguage("en")
                1 -> onSwitchLanguage("vi")
            }
        },
        config = OptionConfig(mode = DisplayMode.LIST)
    )
}




