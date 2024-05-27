package com.mdev.chatapp.domain.result

sealed class ApiResult <T>(val data: Any? = null) {
    data class Success<T>(val response: T? = null) : ApiResult<T>()
    data class Error<T>(val message: Int) : ApiResult<T>()
    data class Unauthorized<T>(val message: String) : ApiResult<T>()
    data class UnknownError<T>(val message: String?): ApiResult<T>()
}