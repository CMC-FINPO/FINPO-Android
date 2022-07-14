package com.finpo.app.ui.community_search

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.MutableLiveData
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import javax.inject.Inject

class CommunitySearchLiveData @Inject constructor() {
    val searchInputText = MutableLiveData("")
    var searchText = ""
    val showInitView = MutableLiveData(true)

    private val _keyBoardSearchEvent = MutableSingleLiveData<Boolean>()
    val keyBoardSearchEvent: SingleLiveData<Boolean> = _keyBoardSearchEvent

    fun clearSearchInputText() {
        searchInputText.value = ""
    }

    fun onEditTextSearchClick(actionId: Int): Boolean {
        return if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchText = searchInputText.value ?: ""
            _keyBoardSearchEvent.setValue(true)
            true
        } else false
    }
}