package com.finpo.app.ui.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.repository.BookmarkRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _categoryData = MutableLiveData<List<ParentCategory>>()
    val categoryData: LiveData<List<ParentCategory>> = _categoryData

    private val _policyList = MutableLiveData<MutableList<PolicyContent>>()
    val policyList: LiveData<MutableList<PolicyContent>> = _policyList

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    private val _goToDetailFragmentEvent = MutableSingleLiveData<Int>()
    val goToDetailFragmentEvent: SingleLiveData<Int> = _goToDetailFragmentEvent

    init {
        _nickname.value = ""
        _policySize.value = 0
    }

    //TODO 코루틴 공부 후 리팩토링
    fun getInitData() {
        viewModelScope.launch {
            val myInfoResponse = myInfoRepository.getMyInfo()
            if(myInfoResponse !is ApiResponse.Success) return@launch
            _nickname.value = myInfoResponse.data.data.nickname ?: ""

            val myCategoryResponse = myInfoRepository.getMyParentCategory()
            if(myCategoryResponse !is ApiResponse.Success) return@launch
            _categoryData.value = myCategoryResponse.data.data

            val myInterestPolicyResponse = myInfoRepository.getMyInterestPolicy()
            if(myInterestPolicyResponse !is ApiResponse.Success) return@launch
            _policyList.value = MutableList(myInterestPolicyResponse.data.data.size) { myInterestPolicyResponse.data.data[it].policy!! }
            _policySize.value = myInterestPolicyResponse.data.data.size
        }
    }

    fun deleteInterestPolicy(data: PolicyContent) {
        viewModelScope.launch {
            val deleteInterestPolicyResponse = bookmarkRepository.deleteInterestPolicy(data.id)
            deleteInterestPolicyResponse.onSuccess {
                _policyList.value?.remove(data)
                _policyList.value = _policyList.value
                _policySize.value = _policyList.value?.size ?: 0
            }
        }
    }

    fun goToDetailFragment(id: Int) {
        _goToDetailFragmentEvent.setValue(id)
    }
}