package com.finpo.app.ui.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.repository.MyInfoRepository
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
): ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _categoryData = MutableLiveData<List<ParentCategory>>()
    val categoryData: LiveData<List<ParentCategory>> = _categoryData

    init {
        getMyNickname()
        getMyCategory()
    }

    private fun getMyNickname() {
        viewModelScope.launch {
            val myInfoResponse = myInfoRepository.getMyInfo()
            myInfoResponse.onSuccess { _nickname.value = data.data.nickname ?: "" }
        }
    }

    private fun getMyCategory() {
        viewModelScope.launch {
            val myCategoryResponse = myInfoRepository.getMyParentCategory()
            myCategoryResponse.onSuccess { _categoryData.value = data.data }
        }
    }
}