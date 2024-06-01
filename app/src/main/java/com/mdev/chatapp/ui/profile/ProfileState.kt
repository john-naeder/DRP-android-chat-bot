package com.mdev.chatapp.ui.profile

import java.time.LocalDate

data class ProfileState (
    val isLoading: Boolean = false,
    val userName: String = "",
    val dateOfBirth:  LocalDate = LocalDate.now(),
    val name: String = "N/A",
    val age: Int = 0,
    val height: Float = 0.0f,
    val weight: Float = 0.0f,
    val bmi: Float = 0.0f,

    val isAgeError: Boolean = false,
    val isAgeErrorMsg: String = "",
    val isSuccessFullUpdated: Boolean = false,
    val isUpdating: Boolean = false,

    val errorMsg: String = "",

)