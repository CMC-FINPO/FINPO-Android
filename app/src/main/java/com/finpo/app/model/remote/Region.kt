package com.finpo.app.model.remote

data class RegionResponse(
    val data: List<Region>
)

data class Region(
    val id: Int,
    val name: String
)

data class RegionRequest(
    val regionId: Int
)

data class RegionInterestResponse(
    val data: List<RegionInterest>
)

data class RegionInterest(
    val id: Int?,
    val name: String?,
    val parent: RegionParent?
)

data class RegionParent(
    val id: Int,
    val name: String
)