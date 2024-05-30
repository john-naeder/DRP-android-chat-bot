package com.mdev.chatapp.data.remote.auth

import com.mdev.chatapp.data.remote.auth.model.ResetPasswordRequest
import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.data.remote.auth.model.TokenResponse
import com.mdev.chatapp.data.remote.auth.model.TokensRefreshResponse
import com.mdev.chatapp.data.remote.auth.model.SendOTPRequest
import com.mdev.chatapp.data.remote.auth.model.VerifyOTPRequest
import retrofit2.Response
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
    ): Response<TokenResponse>

    @GET ("authenticate")
    suspend fun authenticate(
        @Header ("Authorization") token: String
    ): Response<TokenResponse>

    @GET ("refreshToken")
    suspend fun refreshToken(
        @Header ("Authorization") token: String
    ): Response<TokensRefreshResponse>

    @POST ("sendOTP")
    suspend fun sendOTP(
        @Body request: SendOTPRequest
    )
    @POST ("verifyOTP")
    suspend fun verifyOTP(
        @Body request: VerifyOTPRequest
    )

    @POST ("reset/getOTP")
    suspend fun resetPasswordOTP(
        @Body request: SendOTPRequest
    )

    @POST ("reset/password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    )

}