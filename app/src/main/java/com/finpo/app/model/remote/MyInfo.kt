package com.finpo.app.model.remote

data class MyInfoResponse(
    val data: MyInfo
)

data class MyInfo(
    val id: Int?,
    val nickname: String?,
    val gender: String?,
    val profileImg: String?,
    val oAuthType: String?,
    val defaultRegion: RegionInterest?
)
