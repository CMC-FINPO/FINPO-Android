package com.finpo.app.utils

const val INTRO_PAGE_NUM = 9
const val MAX_NAME_LENGTH = 13
const val MAX_NICKNAME_LENGTH = 13
const val MAX_ADDITIONAL_COUNT = 5
const val RETROFIT_TAG = "retrofit2"
val SORT = listOf("modifiedAt,desc", "countOfInterest,desc")

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
    const val BASE_URL: String = "https://dev.finpo.kr/"
}

object PolicyRecyclerViewType {
    const val LOADING = 0
    const val CONTENT = 1
}