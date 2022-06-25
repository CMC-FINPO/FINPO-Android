package com.finpo.app.model.remote

import retrofit2.http.Field

data class StatusPurposeResponse(
    val data: List<StatusPurpose>
)

data class StatusPurpose(
    val id: Int,
    val name: String
)

data class StatusPurposeBody(
    val statusId: Int?,
    val purposeIds: List<Int>?
)
