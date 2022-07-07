package com.finpo.app.ui.participation_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ParticipationListViewModel @Inject constructor() : ViewModel() {
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    private val _policySize = MutableLiveData<Int>()
    val policySize: LiveData<Int> = _policySize

    fun initData() {
        _policySize.value = 0
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }
}