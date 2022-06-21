package com.finpo.app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.model.remote.PolicyList
import com.finpo.app.model.remote.PolicyResponse
import com.finpo.app.repository.PolicyRepository
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val policyRepository: PolicyRepository
): ViewModel() {
    private val _policyList = MutableLiveData<List<PolicyContent?>>()
    val policyList: LiveData<List<PolicyContent?>> = _policyList

    init {
        getPolicy()
    }

    fun getPolicy() {
        viewModelScope.launch {
            val policyResponse = policyRepository.getPolicy(0, listOf("modifiedAt,desc"))
            policyResponse.onSuccess {
                _policyList.value = data.data.content + listOf(null)
            }
        }
    }
}