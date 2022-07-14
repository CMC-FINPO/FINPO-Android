package com.finpo.app.model.remote

data class MyInfoResponse(
    val data: MyInfo
)

data class MyInfo(
    val id: Int?,
    val name: String?,
    val birth: String?,
    val nickname: String?,
    val gender: String?,
    val profileImg: String?,
    val oAuthType: String?,
    val defaultRegion: RegionInterest?
)

data class EditMyInfoRequest(
    val name: String?,
    val nickname: String?,
    val birth: String?,
    val gender: String?
)
