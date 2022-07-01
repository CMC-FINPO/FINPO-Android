package com.finpo.app.model.remote

data class PolicyDetailResponse(
    val data: PolicyDetail
)

data class PolicyDetail(
    val id: Int,
    val title: String?,
    val content: String?,
    val institution: String?,
    val supportScale: String?,
    val support: String?,
    val period: String?,
    val startDate: String?,
    val endDate: String?,
    val process: String?,
    val announcement: String?,
    val detailUrl: String?,
    val openApiType: String?,
    val modifiedAt: String?,
    val category: Category?,
    val region: RegionInterest?,
    val countOfInterest: Int = 0,
    val hits: Int = 0,
    val isInterest: Boolean = false
)
