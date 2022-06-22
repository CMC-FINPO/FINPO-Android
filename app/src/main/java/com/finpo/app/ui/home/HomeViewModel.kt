package com.finpo.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.repository.PolicyRepository
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SORT
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val policyRepository: PolicyRepository,
    val paging: Paging<PolicyContent>
): ViewModel() {

    private val _policyList = MutableLiveData<List<PolicyContent?>>()
    val policyList: LiveData<List<PolicyContent?>> = _policyList

    val spinnerPosition = MutableLiveData<Int>()
    var prevSpinnerPosition = 0

    init {
        addPolicy()
    }

    fun addPolicy() {
        if(paging.isLastPage) return

        viewModelScope.launch {
            val policyResponse = policyRepository.getPolicy(paging.page.value ?: 0, listOf(SORT[spinnerPosition.value ?: 0]))
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.addData())
            }
        }
    }

    fun changePolicy() {
        paging.resetPage()

        viewModelScope.launch {
            val policyResponse = policyRepository.getPolicy(paging.page.value ?: 0, listOf(SORT[spinnerPosition.value ?: 0]))
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.changeData())
            }
        }
    }
}