package com.finpo.app.utils

const val INTRO_PAGE_NUM = 9
const val MAX_NAME_LENGTH = 13
const val MAX_NICKNAME_LENGTH = 13
const val MAX_ADDITIONAL_COUNT = 5
const val MAX_FILTER_REGION_COUNT = 6
const val RETROFIT_TAG = "retrofit2"
val SORT_POLICY = listOf("id,desc", "countOfInterest,desc")
val SORT_COMMUNITY = listOf(listOf("id,desc"), listOf("likes,desc", "id,desc"))
const val COMMENT = 0
const val POST = 1
const val TAB_SMALL = "SMALL"
const val TAB_NORMAL = "NORMAL"
const val COMMUNITY_IMAGE_MAX_COUNT = 5

enum class EditRegionType {
    LIVING, INTEREST
}


object FCM_TYPE {
    const val POLICY = "POLICY"
    const val COMMENT = "COMMENT"
    const val CHILDCOMMENT = "CHILDCOMMENT"
}

object SORT_TYPE {
    const val RECENT = 0
    const val POPULAR = 1
}

object PAGE {
    const val LOGIN = 0
    const val AGREE = 1
    const val DEFAULT_INFO = 2
    const val REGION = 3
    const val INTEREST = 4
    const val REGISTRATION = 5
    const val STATE_PURPOSE = 6
    const val ADDITIONAL_REGION = 7
    const val FINISH = 8
}

object API {
    /**
     * 배포 :  "https://api.finpo.kr/"
     * 개발 : "https://dev.finpo.kr/"
     * */
    const val BASE_URL: String = "https://dev.finpo.kr/"
}

object PolicyRecyclerViewType {
    const val LOADING = 0
    const val CONTENT = 1
}