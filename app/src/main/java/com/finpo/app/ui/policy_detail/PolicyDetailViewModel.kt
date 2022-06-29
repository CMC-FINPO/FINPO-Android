package com.finpo.app.ui.policy_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.PolicyDetailRepository
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolicyDetailViewModel @Inject constructor(
    private val policyDetailRepository: PolicyDetailRepository
): ViewModel() {
    fun getPolicyDetail(id: Int) {
        viewModelScope.launch {
            val policyDetailResponse = policyDetailRepository.getPolicyDetail(id)
            policyDetailResponse.onSuccess {
                Log.d("policyDetail","${data.data}")
            }
        }
    }
}