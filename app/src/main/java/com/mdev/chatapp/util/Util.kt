package com.mdev.chatapp.util

import android.content.Context
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

class Util {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isJWTExpired(jwt: String): Boolean {
            val decodedJWT: DecodedJWT = JWT.decode(jwt)
            val expiresAt: Date = decodedJWT.expiresAt
            return expiresAt.before(Date())
        }
    }
}