package com.mdev.chatapp.data.remote.auth

import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.data.remote.auth.model.TokenResponse
import com.mdev.chatapp.data.remote.auth.model.TokensRefreshResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthApi {
    @POST ("sign-up")
    suspend fun signUp(
        @Body request: SignUpRequest
    )

    @POST ("login")
    suspend fun signIn(
        @Body request: SignInRequest
    ): TokenResponse

    @GET ("authenticate")
    suspend fun authenticate(
        @Header ("Authorization") token: String
    ): TokenResponse

    @GET ("refreshToken")
    suspend fun refreshToken(
        @Header ("Authorization") token: String
    ): TokensRefreshResponse

}