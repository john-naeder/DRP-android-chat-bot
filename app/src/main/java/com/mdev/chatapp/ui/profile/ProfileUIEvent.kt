package com.mdev.chatapp.ui.profile

import java.time.LocalDate

sealed class ProfileUIEvent {
    data class OnNameChanged(val name: String): ProfileUIEvent()
    data class OnDateOfBirthChanged(val dateOfBirth: LocalDate): ProfileUIEvent()
    data class OnHeightChanged(val height: Float): ProfileUIEvent()
    data class OnWeightChanged(val weight: Float): ProfileUIEvent()
    data class OnAgeChanged(val age: Int): ProfileUIEvent()
    data object OnSaveClicked: ProfileUIEvent()
    data object OnCancelClicked: ProfileUIEvent()
    data object OnEditClicked: ProfileUIEvent()
}