package com.finpo.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PolicyContent
import com.finpo.app.model.remote.PolicyResponse
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.PolicyRepository
import com.finpo.app.utils.Paging
import com.finpo.app.utils.SORT
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val policyRepository: PolicyRepository,
    private val myInfoRepository: MyInfoRepository,
    val paging: Paging<PolicyContent>
) : ViewModel() {

    private val _policyList = MutableLiveData<List<PolicyContent?>>()
    val policyList: LiveData<List<PolicyContent?>> = _policyList

    private val _regionIds = MutableLiveData<List<Int>>()
    val regionIds: LiveData<List<Int>> = _regionIds

    val spinnerPosition = MutableLiveData<Int>()
    var prevSpinnerPosition = 0

    init {
        getInitData()
    }

    private fun getInitData() {
        viewModelScope.launch {
            val myRegionResponse = myInfoRepository.getMyRegion()
            myRegionResponse.onSuccess {
                _regionIds.value = List(data.data.size) { data.data[it].region.id ?: 0 }
                addPolicy()
            }
        }
    }

    fun addPolicy() {
        if (paging.isLastPage) return

        viewModelScope.launch {
            val policyResponse = getPolicyResponse()
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.addData()
                )
            }
        }
    }

    fun changePolicy() {
        paging.resetPage()

        viewModelScope.launch {
            val policyResponse = getPolicyResponse()
            policyResponse.onSuccess {
                paging.loadData(
                    data.data.content.toMutableList(),
                    data.data.last, _policyList,
                    paging.changeData()
                )
            }
        }
    }

    private suspend fun getPolicyResponse(): ApiResponse<PolicyResponse> {
        return policyRepository.getPolicy(
            region = _regionIds.value ?: listOf(),
            page = paging.page.value ?: 0,
            sort = listOf(SORT[spinnerPosition.value ?: 0])
        )
    }
}