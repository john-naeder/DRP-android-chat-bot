package com.mdev.chatapp.util

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

class ValidateUtil {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidUsername(username: String): Boolean {
            return username.length >= 4 && username.matches(Regex("^[a-zA-Z0-9]*$"))
        }
        fun isPasswordValid(password: String): Boolean {
            return password.length >= 6 && password.matches(Regex(".*[^a-zA-Z0-9 ].*"))
        }

        fun isJWTExpired(jwt: String): Boolean {
            val decodedJWT: DecodedJWT = JWT.decode(jwt)
            val expiresAt: Date = decodedJWT.expiresAt
            return expiresAt.before(Date())
        }
    }
}