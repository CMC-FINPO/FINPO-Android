package com.finpo.app.model.remote

data class OpenApiResponse(
    val data: List<OpenApi>
)
data class OpenApi(
    val content: String
)