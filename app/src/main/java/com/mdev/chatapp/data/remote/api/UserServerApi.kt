package com.mdev.chatapp.data.remote.api

import com.mdev.chatapp.data.remote.auth.model.ResetPasswordRequest
import com.mdev.chatapp.data.remote.auth.model.SignInRequest
import com.mdev.chatapp.data.remote.auth.model.SignUpRequest
import com.mdev.chatapp.data.remote.auth.model.TokenResponse
import com.mdev.chatapp.data.remote.auth.model.TokensRefreshResponse
import com.mdev.chatapp.data.remote.auth.model.SendOTPRequest
import com.mdev.chatapp.data.remote.auth.model.VerifyOTPRequest
import com.mdev.chatapp.data.remote.user.model.UpdateUserProfileRequest
import com.mdev.chatapp.data.remote.user.model.UserProfileRequest
import com.mdev.chatapp.data.remote.user.model.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface UserServerApi {
    @POST ("sign-up")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<Unit>

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
    ): Response<Unit>
    @POST ("verifyOTP")
    suspend fun verifyOTP(
        @Body request: VerifyOTPRequest
    ): Response<Unit>

    @POST ("reset/getOTP")
    suspend fun resetPasswordOTP(
        @Body request: SendOTPRequest
    ): Response<Unit>

    @POST ("reset/password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<Unit>


    @POST("account/getInfo")
    suspend fun getUserInfo(
        @Body userProfileRequest: UserProfileRequest
    ): Response<UserProfileResponse>

    @POST("account/updateInfo")
    suspend fun updateUserInfo(
        @Body updateUserProfileRequest: UpdateUserProfileRequest
    ): Response<Unit>

}