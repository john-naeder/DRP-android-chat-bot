package com.mdev.chatapp.util

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

class Jwt {
    companion object {
        fun isJWTExpired(jwt: String): Boolean {
            val decodedJWT: DecodedJWT = JWT.decode(jwt)
            val expiresAt: Date = decodedJWT.expiresAt
            return expiresAt.before(Date())
        }
    }
}