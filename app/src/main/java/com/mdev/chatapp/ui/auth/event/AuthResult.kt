package com.mdev.chatapp.ui.auth.event


sealed class AuthResult <T> (val data: T? = null) {
    class Authorized<T>(data: T? = null) : AuthResult<T>(data)
    class Unauthorized<T>(val message: String) : AuthResult<T>()
    class Error<T>(val message: String) : AuthResult<T>()
    class UnknownError<T>(val message: String): AuthResult<T>()
}