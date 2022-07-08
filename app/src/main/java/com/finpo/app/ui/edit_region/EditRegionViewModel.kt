package com.finpo.app.ui.edit_region

import android.util.Log
import androidx.lifecycle.*
import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.repository.EditRegionRepository
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.ui.common.RegionViewModel
import com.finpo.app.utils.*
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRegionViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    private val editRegionRepository: EditRegionRepository,
    val interestRegionViewModel: RegionViewModel,
    val livingRegionViewModel: RegionViewModel
) : ViewModel() {
    var nickname = ""

    private val _viewpagerPosition = MutableLiveData<EditRegionType>()
    val viewpagerPosition: LiveData<EditRegionType> = _viewpagerPosition

    private val _goToMyInfoFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToMyInfoFragmentEvent: SingleLiveData<Boolean> = _goToMyInfoFragmentEvent

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    val isEditRegionButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSourceList(_viewpagerPosition, interestRegionViewModel._regionCount, livingRegionViewModel._regionCount) {
            isEditRegionValid()
        }
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    private fun isEditRegionValid(): Boolean
    = (_viewpagerPosition.value == EditRegionType.INTEREST && interestRegionViewModel._regionCount.value != 0) ||
            (_viewpagerPosition.value == EditRegionType.LIVING && livingRegionViewModel._regionCount.value != 0)

    fun editButtonClick() {
        when(_viewpagerPosition.value!!) {
            EditRegionType.INTEREST -> editInterestRegion()
            EditRegionType.LIVING -> editLivingRegion()
        }
    }

    private fun editInterestRegion() {
        viewModelScope.launch {
            val regionRequests = MutableList(interestRegionViewModel.regionIds.size) { RegionRequest(interestRegionViewModel.regionIds[it] ?: 0) }
            val editInterestRegionResponse = editRegionRepository.editMyInterestRegion(regionRequests)
            editInterestRegionResponse.onSuccess { _goToMyInfoFragmentEvent.setValue(true) }
        }
    }

    private fun editLivingRegion() {
        viewModelScope.launch {
            val editLivingRegionResponse = editRegionRepository.editMyLivingRegion(livingRegionViewModel.regionIds[0] ?: 0)
            editLivingRegionResponse.onSuccess { _goToMyInfoFragmentEvent.setValue(true) }
        }
    }

    fun setViewpagerTypeInterest() {
        _viewpagerPosition.value = EditRegionType.INTEREST
    }

    fun setViewpagerTypeLiving() {
        _viewpagerPosition.value = EditRegionType.LIVING
    }

    fun getMyInterestRegion() {
        val interestRegionText = MutableList(MAX_ADDITIONAL_COUNT) {""}
        val livingRegionText = MutableList(MAX_ADDITIONAL_COUNT) {""}
        viewModelScope.launch {
            val myRegionResponse = myInfoRepository.getMyRegion()
            if(myRegionResponse !is ApiResponse.Success) return@launch
            var idx = 0
            for(regionData in myRegionResponse.data.data) {
                if(regionData.isDefault == true) {
                    livingRegionText[0] = interestRegionViewModel.formatRegionName(regionData.region)
                    livingRegionViewModel.regionIds.add(regionData.region.id)
                }
                else {
                    interestRegionText[idx] = interestRegionViewModel.formatRegionName(regionData.region)
                    interestRegionViewModel.regionIds.add(regionData.region.id)
                    idx += 1
                }
            }
            livingRegionViewModel.setRegionCount(1)
            livingRegionViewModel.setRegionTextList(livingRegionText)
            interestRegionViewModel.setRegionCount(idx)
            interestRegionViewModel.setRegionTextList(interestRegionText)
        }
    }
}