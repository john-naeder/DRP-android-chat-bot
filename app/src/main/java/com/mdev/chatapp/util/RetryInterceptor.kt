package com.mdev.chatapp.util

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    private var retryCount = 0

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var response: Response? = null
        var responseSuccessful = false
        while (!responseSuccessful && retryCount < maxRetries) {
            try {
                response = chain.proceed(request)
                responseSuccessful = response.isSuccessful
            } catch (e: Exception) {
                retryCount++
                if (retryCount == maxRetries) {
                    throw e
                }
            }
        }
        return response!!
    }
}

