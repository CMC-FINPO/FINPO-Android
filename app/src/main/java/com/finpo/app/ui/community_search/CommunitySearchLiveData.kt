package com.finpo.app.ui.community_search

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class CommunitySearchLiveData @Inject constructor() {
    val searchInputText = MutableLiveData("")
    val showInitView = MutableLiveData(true)
}