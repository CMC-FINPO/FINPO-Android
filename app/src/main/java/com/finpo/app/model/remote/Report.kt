package com.finpo.app.model.remote

data class ReportReasonResponse(
    val data: List<IdReason>
)

data class IdReason(
    val id: Int,
    val reason: String
)