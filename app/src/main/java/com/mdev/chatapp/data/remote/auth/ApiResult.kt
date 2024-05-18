package com.mdev.chatapp.data.remote.auth

sealed class ApiResult <T> (val data: T? = null) {
    class Authorized<T>(data: T? = null) : ApiResult<T>(data)
    class Unauthorized<T>(data: T? = null) : ApiResult<T>(data)
    class Error<T>(val message: String, data: T? = null) : ApiResult<T>(data)
    class UnknownError<T>(val message: String): ApiResult<T>()
}