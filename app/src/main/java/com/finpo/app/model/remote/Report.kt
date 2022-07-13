package com.finpo.app.model.remote

data class ReportReasonResponse(
    val data: List<IdReason>
)

data class IdReason(
    val id: Int,
    val reason: String
)

data class ReportRequest(
    val report: Id
)

data class Id(
    val id: String
)