package com.finpo.app.model.remote

data class StatusPurposeResponse(
    val data: List<StatusPurpose>
)

data class StatusPurpose(
    val id: Int,
    val name: String
)
