package com.finpo.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.repository.PolicyRepository
import com.finpo.app.utils.MutableSingleLiveData
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
                //TODO - REFACTOR, 함수 하나만 사용하게 만들기
                paging.deleteLoading()
                val policyList: MutableList<PolicyContent?> = data.data.content.toMutableList()
                paging.addLoadingView(policyList)
                paging.addData(policyList)
                paging.isLastPage = data.data.last
                _policyList.value = paging.itemList.value ?: listOf()
                paging.nextPage()
            }
        }
    }

    fun changePolicy() {
        paging.resetPage()

        viewModelScope.launch {
            val policyResponse = policyRepository.getPolicy(paging.page.value ?: 0, listOf(SORT[spinnerPosition.value ?: 0]))
            policyResponse.onSuccess {
                //TODO - REFACTOR, 함수 하나만 사용하게 만들기
                paging.deleteLoading()
                val policyList: MutableList<PolicyContent?> = data.data.content.toMutableList()
                paging.addLoadingView(policyList)
                paging.changeData(policyList)
                paging.isLastPage = data.data.last
                _policyList.value = paging.itemList.value ?: listOf()
                paging.nextPage()
            }
        }
    }
}