package com.finpo.app.ui.edit_region

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.RegionRepository
import com.finpo.app.ui.common.RegionViewModel
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRegionViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    val interestRegionViewModel: RegionViewModel,
    val livingRegionViewModel: RegionViewModel
) : ViewModel() {
    var nickname = ""
    private val _myLivingRegionText = MutableLiveData<String>()

    fun getMyInterestRegion() {
        val interestRegionText = MutableList(MAX_ADDITIONAL_COUNT) {""}
        viewModelScope.launch {
            val myRegionResponse = myInfoRepository.getMyRegion()
            if(myRegionResponse !is ApiResponse.Success) return@launch
            var idx = 0
            for(regionData in myRegionResponse.data.data) {
                if(regionData.isDefault == true) _myLivingRegionText.value = interestRegionViewModel.formatRegionName(regionData.region)
                else {
                    interestRegionText[idx] = interestRegionViewModel.formatRegionName(regionData.region)
                    interestRegionViewModel.regionIds.add(regionData.region.id)
                    idx += 1
                }
            }
            interestRegionViewModel.setRegionCount(idx)
            interestRegionViewModel.setRegionTextList(interestRegionText)
        }
    }
}