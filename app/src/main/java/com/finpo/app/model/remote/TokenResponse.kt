package com.finpo.app.model.remote

data class RegisterResponse(
    val data: TokenResponse
)

data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpiresIn: Long?
)