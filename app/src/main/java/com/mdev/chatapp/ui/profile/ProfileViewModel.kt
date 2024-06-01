package com.mdev.chatapp.ui.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mdev.chatapp.domain.repository.remote.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.chatapp.R
import com.mdev.chatapp.data.remote.user.model.UserProfileRequest
import com.mdev.chatapp.domain.result.ApiResult
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.StringAndDateConvertor
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val dataStoreHelper: DataStoreHelper
): ViewModel() {
    var state by mutableStateOf(ProfileState())


    init {
        viewModelScope.launch {
            getUserInfo()
        }
    }

    fun onEvent(event: ProfileUIEvent){
        when(event){
            is ProfileUIEvent.OnHeightChanged-> {
                state = state.copy(height = event.height)
            }
            is ProfileUIEvent.OnWeightChanged-> {
                state = state.copy(weight = event.weight)
            }
            is ProfileUIEvent.OnNameChanged -> {
                state = state.copy(name = event.name)
            }
            is ProfileUIEvent.OnAgeChanged -> {
                state = state.copy(age = event.age)
            }
            is ProfileUIEvent.OnDateOfBirthChanged -> {
                state = state.copy(dateOfBirth = event.dateOfBirth)
            }
            is ProfileUIEvent.OnEditClicked -> {
                state = state.copy(isUpdating = !state.isUpdating)
            }
            is ProfileUIEvent.OnSaveClicked -> {
                saveInfo()
            }
            is ProfileUIEvent.OnCancelClicked -> {
                resetState()
            }
        }
    }

    private fun saveInfo(){
        viewModelScope.launch{
            state = state.copy(isLoading = true)
            Log.d("ProfileViewModel", "saveInfo: ${state.isLoading}")
            when(val result = userProfileRepository.updateUserInfo(
                state.dateOfBirth.toString(),
                state.height,
                state.name,
                state.userName,
                state.weight)
            ){
                is ApiResult.Success -> {
                    getUserInfo()
                    state = state.copy(isSuccessFullUpdated = true, isUpdating = false)
                }
                is ApiResult.UnknownError -> {
                    resetState()
                    Log.d("ProfileViewModel", "saveInfo2: ${result.message}")
                    state = state.copy(isSuccessFullUpdated = false, isUpdating = false)
                    return@launch
                }
                is ApiResult.LogError -> {
                    resetState()
                    Log.d("ProfileViewModel", "saveInfo3: ${result.message}")
                    state =  state.copy(isSuccessFullUpdated = false, isUpdating = false)
                    return@launch
                }
                else -> {
                    // Do nothing
                }
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun resetState(){
        state = ProfileState(userName = state.userName)
    }
    private fun getUserInfo(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val username = dataStoreHelper.getString(Constants.CURRENT_USER)
            when(val response = userProfileRepository.getUserInfo(username!!)){

                is ApiResult.Success -> {
                    Log.d("ProfileViewModel", "init success: ${response.response} ")
                    val name = response.response?.data?.name ?: "N/A"

                    val dateOfBirth = StringAndDateConvertor.stringToDate(
                        if (response.response?.data?.dateOfBirth != null) response.response.data.dateOfBirth
                        else "01-01-2000"
                    )

                    val age = response.response?.data?.age ?: 0
                    val height = response.response?.data?.height ?: 0f
                    val weight = response.response?.data?.weight ?: 0f
                    val bmi = response.response?.data?.bmi ?: 0f
                    state = state.copy(
                        name = name,
                        userName = "$username",
                        age = age,
                        height = height,
                        weight = weight,
                        bmi = bmi,
                        dateOfBirth = dateOfBirth
                    )
                }
                is ApiResult.UnknownError -> {
                    Log.d("ProfileViewModel", "init Unknown: ${response.message}")
                    state = state.copy(errorMsg = response.message)
                }
                is ApiResult.LogError -> {
                    Log.d("ProfileViewModel", "init api: ${response.message} ")
                    state = state.copy(errorMsg = response.message)
                }
                else -> {
                    // Do nothing
                }
            }
            state = state.copy(isLoading = false)
        }
    }
}