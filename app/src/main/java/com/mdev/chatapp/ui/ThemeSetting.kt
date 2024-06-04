package com.mdev.chatapp.ui
import android.os.Parcelable
import androidx.annotation.StringRes
import com.mdev.chatapp.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ThemeSetting(
    @StringRes val nameStringResource: Int
) : Parcelable {
    Dark(R.string.theme_dark), Light(R.string.theme_light), System(R.string.theme_system);
}