package com.finpo.app.model.remote

data class RegionResponse(
    val data: List<Region>
)

data class Region(
    val id: Int,
    val name: String
)