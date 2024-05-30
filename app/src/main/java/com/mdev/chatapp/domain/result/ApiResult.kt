package com.mdev.chatapp.domain.result

sealed class ApiResult <out T>(val data: Any? = null) {
    data class Success<out T>(val response: T? = null) : ApiResult<T>()
    data class Error(val message: Int) : ApiResult<Nothing>()
    data class LogError(val message: String): ApiResult<Nothing>()
    data class UnknownError(val message: String?): ApiResult<Nothing>()
}