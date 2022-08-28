package com.finpo.app.ui.participation_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParticipationPolicy
import com.finpo.app.repository.BookmarkRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.PolicyDetailRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.finpo.app.utils.deepCopy
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipationListViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    private val policyDetailRepository: PolicyDetailRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    private val _policyList = MutableLiveData<MutableList<ParticipationPolicy>>()
    val policyList: LiveData<MutableList<ParticipationPolicy>> = _policyList

    private val _isDeleteMode = MutableLiveData<Boolean>()
    val isDeleteMode: LiveData<Boolean> = _isDeleteMode

    private val _changeToDeleteModeEvent = MutableSingleLiveData<Boolean>()
    val changeToDeleteModeEvent: SingleLiveData<Boolean> = _changeToDeleteModeEvent

    private val _deleteItemClickEvent = MutableSingleLiveData<ParticipationPolicy>()
    val deleteItemClickEvent: SingleLiveData<ParticipationPolicy> = _deleteItemClickEvent

    val editMemoText = MutableLiveData<String?>()

    private val _memoId = MutableLiveData<Int>()
    val memoId: LiveData<Int> = _memoId

    private val _isEditMode = MutableLiveData<Boolean>()
    val isEditMode: LiveData<Boolean> = _isEditMode

    private val _showBottomSheetEvent = MutableSingleLiveData<Boolean>()
    val showBottomSheetEvent: SingleLiveData<Boolean> = _showBottomSheetEvent

    private val _dismissBottomSheetEvent = MutableSingleLiveData<Boolean>()
    val dismissBottomSheetEvent: SingleLiveData<Boolean> = _dismissBottomSheetEvent

    private val _showBookmarkCountMaxToastEvent = MutableSingleLiveData<Boolean>()
    val showBookmarkCountMaxToastEvent: SingleLiveData<Boolean> = _showBookmarkCountMaxToastEvent

    private val _goToPolicyDetailEvent = MutableSingleLiveData<Int>()
    val goToPolicyDetailEvent: SingleLiveData<Int> = _goToPolicyDetailEvent

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    fun initData() {
        _policySize.value = 0
        _isDeleteMode.value = false
        _isEditMode.value = false
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun goToPolicyDetail(id: Int) {
        _goToPolicyDetailEvent.setValue(id)
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    fun dismissBottomDialog() {
        _dismissBottomSheetEvent.setValue(true)
    }

    fun editMemoClick(id: Int, memo: String?) {
        editMemoText.value = memo ?: ""
        _isEditMode.value = memo != null
        _memoId.value = id
        _showBottomSheetEvent.setValue(true)
    }

    fun editMemo() {
        viewModelScope.launch {
            val editMemoResponse = policyDetailRepository.editParticipationPolicyMemo(_memoId.value ?: 0, editMemoText.value ?: "")
            editMemoResponse.onSuccess {
                val changeIdx = _policyList.value?.indexOfFirst { it.id == _memoId.value } ?: -1
                if(changeIdx == -1) return@onSuccess
                val tempPolicyList = _policyList.value!!.deepCopy()
                tempPolicyList[changeIdx].memo = editMemoText.value
                _policyList.value = tempPolicyList
                _dismissBottomSheetEvent.setValue(true)
            }
        }
    }

    fun deleteClick() {
        _isDeleteMode.value = !_isDeleteMode.value!!
        _changeToDeleteModeEvent.setValue(true)
    }

    fun deleteItemClick(data: ParticipationPolicy) {
        _deleteItemClickEvent.setValue(data)
    }

    fun deleteParticipationPolicy(data: ParticipationPolicy) {
        viewModelScope.launch {
            val deleteResponse = myInfoRepository.deleteMyParticipationPolicy(data.id)
            deleteResponse.onSuccess {
                val tempPolicyList = _policyList.value!!.deepCopy()
                tempPolicyList.remove(data)
                _policyList.value = tempPolicyList
                _policySize.value = _policySize.value?.minus(1)
            }
        }
    }

    fun getMyParticipationPolicy() {
        viewModelScope.launch {
            val myParticipationResponse = myInfoRepository.getMyParticipationPolicy()
            myParticipationResponse.onSuccess {
                _policyList.value = data.data.toMutableList()
                _policySize.value = data.data.size
            }
        }
    }

    fun bookmarkClick(tempData: ParticipationPolicy) {
        val data = tempData.copy()
        viewModelScope.launch {
            val response = if(data.policy?.isInterest == false) bookmarkRepository.addInterestPolicy(data.policy.id)
            else bookmarkRepository.deleteInterestPolicy(data.policy!!.id)

            response.onSuccess {
                val tempPolicyList = _policyList.value!!.deepCopy()
                tempPolicyList.find { it == data }?.policy?.isInterest = !data.policy.isInterest
                _policyList.value = tempPolicyList
            }.onError { if(statusCode.code == 400) _showBookmarkCountMaxToastEvent.setValue(true) }
        }
    }
}