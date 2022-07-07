package com.finpo.app.model.remote

data class PolicyResponse(
    val data: PolicyList
)

data class ParticipationPolicyResponse(
    val data: PolicyContent?
)

data class ParticipationPolicy(
    val id: Int,
    val policy: PolicyContent?,
    var memo: String?
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
    val region: RegionInterest,
    var isInterest: Boolean = false
)

data class PolicyId(
    val policyId: Int
)

data class MyInterestPolicyResponse(
    val data: List<ParticipationPolicy>
)
