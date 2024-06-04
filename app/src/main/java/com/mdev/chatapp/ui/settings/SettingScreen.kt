package com.mdev.chatapp.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowDropDownCircle
import androidx.compose.material.icons.twotone.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.mdev.chatapp.R
import com.mdev.chatapp.ui.common.BaseScreen
import com.mdev.chatapp.ui.theme.DefaultShape
import androidx.compose.material3.Text as Text1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navDrawerViewModel: NavigateDrawerViewModel,
    onBackClick: () -> Unit,
    onLogout: (Route) -> Unit,
    onNavigateTo: (Route) -> Unit,
    onSwitchTheme: () -> Unit,
    onSwitchLanguage: (String) -> Unit,
    settingViewmodel: SettingViewModel

){
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItem = Route.AboutScreen
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

                    }
                )
            }

        }
    )
}

@Composable
fun SettingContent(
    state: SettingState,
    onSwitchTheme: () -> Unit,
    onSwitchLanguage: (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Text1(
                text = stringResource(id =  R.string.themee_settings),
                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            ThemeSwitcher(
                darkTheme = true,
                size = 40.dp,
                onClick = onSwitchTheme
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text1(
                text = stringResource(id = R.string.themee_settings),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}





