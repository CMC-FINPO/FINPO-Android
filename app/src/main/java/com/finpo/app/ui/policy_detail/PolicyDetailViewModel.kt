package com.finpo.app.ui.policy_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PolicyDetail
import com.finpo.app.repository.BookmarkRepository
import com.finpo.app.repository.PolicyDetailRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    private val policyDetailRepository: PolicyDetailRepository,
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {
    private val _policyDetailData = MutableLiveData<PolicyDetail>()
    val policyDetailData: LiveData<PolicyDetail> = _policyDetailData

    private val _backClickEvent = MutableSingleLiveData<Boolean>()
    val backClickEvent: SingleLiveData<Boolean> = _backClickEvent

    fun backClick() {
        _backClickEvent.setValue(true)
    }

    fun getPolicyDetail(id: Int) {
        viewModelScope.launch {
            val policyDetailResponse = policyDetailRepository.getPolicyDetail(id)
            policyDetailResponse.onSuccess {
                _policyDetailData.value = data.data!!
            }
        }
    }

    fun setBookmark() {
        viewModelScope.launch {
            val bookmarkResponse = if (policyDetailData.value?.isInterest == true)
                bookmarkRepository.deleteInterestPolicy(policyDetailData.value?.id ?: 0)
            else bookmarkRepository.addInterestPolicy(policyDetailData.value?.id ?: 0)

            bookmarkResponse.onSuccess {
                policyDetailData.value!!.isInterest = !policyDetailData.value!!.isInterest
                _policyDetailData.value = policyDetailData.value
            }
        }
    }
}