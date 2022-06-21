package com.finpo.app.model.remote

data class PolicyResponse(
    val data: PolicyList
)

data class PolicyList(
    val content: List<PolicyContent>,
    val totalElements: Int
)

data class PolicyContent(
    val id: Int,
    val title: String,
    val institution: String,
    val region: RegionInterest
)
