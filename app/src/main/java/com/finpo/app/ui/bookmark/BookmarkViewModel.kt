package com.finpo.app.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.ParticipationPolicy
import com.finpo.app.model.remote.PolicyContent
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

    private val _policyList = MutableLiveData<List<PolicyContent>>()
    val policyList: LiveData<List<PolicyContent>> = _policyList

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    init {
        getMyNickname()
        getMyCategory()
        getMyInterestPolicy()
        _policySize.value = 0
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

    fun getMyInterestPolicy() {
        viewModelScope.launch {
            val myInterestPolicyResponse = myInfoRepository.getMyInterestPolicy()
            myInterestPolicyResponse.onSuccess {
                _policyList.value = List(data.data.size) { data.data[it].policy!! }
                _policySize.value = data.data.size
            }
        }
    }
}