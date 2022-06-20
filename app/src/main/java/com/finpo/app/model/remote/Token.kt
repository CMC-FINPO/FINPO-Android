package com.finpo.app.model.remote

data class TokenResponse(
    val data: Token
)

data class Token(
    val name: String?,
    val nickname: String,
    val birth: String?,
    val gender: String?,
    val profileImg: String?,
    val accessToken: String?,
    val refreshToken: String?,
    val accessTokenExpiresIn: Long?,
    val oAuthType: String?
)

data class RequestTokenBody(
    val accessToken: String,
    val refreshToken: String
)