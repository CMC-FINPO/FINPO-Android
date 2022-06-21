package com.finpo.app.model.remote

data class PolicyResponse(
    val data: PolicyList
)

data class PolicyList(
    val content: List<PolicyContent>,
    val totalElements: Int,
    val last: Boolean
)

data class PolicyContent(
    val id: Int,
    val title: String,
    val institution: String,
    val region: RegionInterest
)
