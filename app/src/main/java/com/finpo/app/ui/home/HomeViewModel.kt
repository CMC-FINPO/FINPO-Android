package com.finpo.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.repository.PolicyRepository
import com.finpo.app.utils.Paging
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val policyRepository: PolicyRepository,
    private val paging: Paging<PolicyContent>
): ViewModel() {

    private val _policyList = MutableLiveData<List<PolicyContent?>>()
    val policyList: LiveData<List<PolicyContent?>> = _policyList

    init {
        getPolicy()
    }

    fun getPolicy() {
        if(paging.isLastPage) return

        viewModelScope.launch {
            val policyResponse = policyRepository.getPolicy(paging.page.value ?: 0, listOf("modifiedAt,desc"))
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
}