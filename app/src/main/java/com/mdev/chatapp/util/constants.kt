package com.mdev.chatapp.util

object Constants {

    // Api Base Links
    const val AUTH_API_BASE_LINK = "https://drpteam.vercel.app/v1/api/"

    // Key
    const val CHAT_KEY_API = "pat_XLaPmpM8ZAlkYRt65NzbVIFSqZc2NgREb8G05CMvYqbsbgQ3wIVqIWcgYYOqfGef"

    // Datastore Keys
    const val USER_SETTINGS = "user_setting"
    const val CURRENT_USER =  "current_user"
    const val APP_ENTRY = "app_entry"
    const val JWT = "jwt_"
    const val JWT_REFRESH = "jwt_refresh_"

    // Error Messages
    const val TOKEN_NOT_FOUND = "Token found"
    const val NO_LOGIN_USER = "No login in user"
    const val VALIDATE_ERROR_EMPTY_FIELD = "Please fill in all fields"
    const val PASSWORD_NOT_MATCH = "Password and re-password do not match"

    // Database
    const val ACCOUNT_DATABASE_NAME = "account_database"
}