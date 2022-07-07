package com.finpo.app.ui.participation_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParticipationPolicy
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.PolicyDetailRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipationListViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    private val policyDetailRepository: PolicyDetailRepository
) : ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    private val _policyList = MutableLiveData<MutableList<ParticipationPolicy>>()
    val policyList: LiveData<MutableList<ParticipationPolicy>> = _policyList

    private val _isDeleteMode = MutableLiveData<Boolean>()
    val isDeleteMode: LiveData<Boolean> = _isDeleteMode

    private val _deleteBtnClickEvent = MutableSingleLiveData<Boolean>()
    val deleteBtnClickEvent: SingleLiveData<Boolean> = _deleteBtnClickEvent

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

    private val _updateRecyclerViewItemEvent = MutableSingleLiveData<Pair<Int, ParticipationPolicy>>()
    val updateRecyclerViewItemEvent: SingleLiveData<Pair<Int, ParticipationPolicy>> = _updateRecyclerViewItemEvent

    private val _goToPolicyDetailEvent = MutableSingleLiveData<Int>()
    val goToPolicyDetailEvent: SingleLiveData<Int> = _goToPolicyDetailEvent

    fun initData() {
        _policySize.value = 0
        _isDeleteMode.value = false
        _isEditMode.value = false
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
                _policyList.value!![changeIdx].memo = editMemoText.value
                _policyList.value = _policyList.value
                _updateRecyclerViewItemEvent.setValue(Pair(changeIdx, _policyList.value!![changeIdx]))
                _dismissBottomSheetEvent.setValue(true)
            }
        }
    }

    fun deleteClick() {
        _isDeleteMode.value = !_isDeleteMode.value!!
        _deleteBtnClickEvent.setValue(true)
    }

    fun deleteItemClick(data: ParticipationPolicy) {
        _deleteItemClickEvent.setValue(data)
    }

    fun deleteParticipationPolicy(data: ParticipationPolicy) {
        viewModelScope.launch {
            val deleteResponse = myInfoRepository.deleteMyParticipationPolicy(data.id)
            deleteResponse.onSuccess {
                _policyList.value?.remove(data)
                _policyList.value = _policyList.value
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
}