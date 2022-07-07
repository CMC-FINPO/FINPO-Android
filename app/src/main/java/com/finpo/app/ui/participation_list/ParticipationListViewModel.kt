package com.finpo.app.ui.participation_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.ParticipationPolicy
import com.finpo.app.repository.MyInfoRepository
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipationListViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    private val _policyList = MutableLiveData<MutableList<ParticipationPolicy>>()
    val policyList: LiveData<MutableList<ParticipationPolicy>> = _policyList

    private val _isDeleteMode = MutableLiveData<Boolean>()
    val isDeleteMode: LiveData<Boolean> = _isDeleteMode

    fun initData() {
        _policySize.value = 0
        _isDeleteMode.value = false
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
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