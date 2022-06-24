package com.finpo.app.model.remote

import com.google.gson.annotations.SerializedName

data class GoogleToken(
    @SerializedName("access_token") val accessToken: String = ""
)
