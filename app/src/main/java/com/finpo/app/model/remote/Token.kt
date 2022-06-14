package com.finpo.app.model.remote

data class TokenResponse(
    val data: Token
)

data class Token(
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpiresIn: Long?
)

data class RequestTokenBody(
    val accessToken: String,
    val refreshToken: String
)