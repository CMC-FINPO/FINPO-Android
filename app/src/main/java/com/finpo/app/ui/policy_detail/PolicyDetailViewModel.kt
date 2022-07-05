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

    private val _showBottomDialogEvent = MutableSingleLiveData<Boolean>()
    val showBottomDialogEvent: SingleLiveData<Boolean> = _showBottomDialogEvent

    private val _dismissBottomDialogEvent = MutableSingleLiveData<Boolean>()
    val dismissBottomDialogEvent: SingleLiveData<Boolean> = _dismissBottomDialogEvent

    private val _overlapParticipationEvent = MutableSingleLiveData<Boolean>()
    val overlapParticipationEvent: SingleLiveData<Boolean> = _overlapParticipationEvent

    private val _addParticipationMemoSuccessEvent = MutableSingleLiveData<Boolean>()
    val addParticipationMemoSuccessEvent: SingleLiveData<Boolean> = _addParticipationMemoSuccessEvent

    private val _bottomSheetPage = MutableLiveData<Int>()
    val bottomSheetPage: LiveData<Int> = _bottomSheetPage

    val participationPolicyMemo = MutableLiveData<String>()

    private var participationPolicyId = 0

    fun addParticipationPolicyMemo() {
        viewModelScope.launch {
            val addParticipationPolicyMemoResponse = policyDetailRepository.editParticipationPolicyMemo(participationPolicyId, participationPolicyMemo.value ?: "")
            addParticipationPolicyMemoResponse.onSuccess {
                _dismissBottomDialogEvent.setValue(true)
                _addParticipationMemoSuccessEvent.setValue(true)
            }
        }
    }

    fun goToParticipationPolicyMemoPage() {
        _bottomSheetPage.value = _bottomSheetPage.value?.plus(1)
    }

    fun addParticipationPolicy() {
        viewModelScope.launch {
            val addParticipationResponse = policyDetailRepository.addParticipationPolicy(_policyDetailData.value?.id ?: 0)
            addParticipationResponse.onSuccess {
                if(data.data == null) {
                    _dismissBottomDialogEvent.setValue(true)
                    _overlapParticipationEvent.setValue(true)
                } else {
                    participationPolicyId = data.data!!.id
                    _bottomSheetPage.value = _bottomSheetPage.value?.plus(1)
                }
            }
        }
    }

    fun dismissBottomDialog() {
        _dismissBottomDialogEvent.setValue(true)
    }

    fun showBottomDialog() {
        resetBottomSheetPage()
        participationPolicyMemo.value = ""
        _showBottomDialogEvent.setValue(true)
    }

    private fun resetBottomSheetPage() {
        _bottomSheetPage.value = 0
    }

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