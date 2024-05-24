package com.mdev.chatapp.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.domain.user_entry.app_entry.AppEntryUserCase
import com.mdev.chatapp.ui.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUserCase: AppEntryUserCase
) : ViewModel(){
    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _startDestination = mutableStateOf(Route.AppStart.route)
    val startDestination: State<String> = _startDestination

    init {
        appEntryUserCase.readAppEntry().onEach {shouldStartFromAuthScreen ->
            if(shouldStartFromAuthScreen){
                _startDestination.value = Route.AuthNavigator.route
            } else {
                _startDestination.value = Route.AppStart.route
            }
            delay(500)
            _splashCondition.value = false
        }.launchIn(viewModelScope)
    }
}