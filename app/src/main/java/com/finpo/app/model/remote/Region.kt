package com.finpo.app.model.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegionResponse(
    val data: List<Region>
)

data class Region(
    val id: Int,
    val name: String,
    val status: Boolean = false
)

data class RegionRequest(
    val regionId: Int
)

data class RegionInterestResponse(
    val data: List<RegionInterest>
)

data class MyRegionResponse(
    val data: List<MyRegion>
) : Serializable

data class MyRegion(
    val region: RegionInterest,
    val isDefault: Boolean?
)

data class RegionInterest(
    val id: Int?,
    val name: String?,
    val parent: RegionParent?
)

data class RegionParent(
    val id: Int,
    val name: String?
)