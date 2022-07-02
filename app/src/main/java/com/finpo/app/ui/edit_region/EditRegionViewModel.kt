package com.finpo.app.ui.edit_region

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.local.IdName
import com.finpo.app.model.remote.RegionInterest
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.finpo.app.utils.MAX_FILTER_REGION_COUNT
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRegionViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    private val _myInterestRegionTextList = MutableLiveData<MutableList<String>>()
    val myInterestRegionTextList: LiveData<MutableList<String>> = _myInterestRegionTextList

    val myInterestRegionIds: MutableList<Int?> = mutableListOf()

    private val _myInterestRegionCount = MutableLiveData<Int>()
    val myInterestRegionCount: LiveData<Int> = _myInterestRegionCount

    private val _myLivingRegionText = MutableLiveData<String>()
    val myLivingRegionText: LiveData<String> = _myLivingRegionText

    var nickname = ""

    fun getMyInterestRegion() {
        val interestRegionText = MutableList(MAX_ADDITIONAL_COUNT) {""}
        viewModelScope.launch {
            val myRegionResponse = myInfoRepository.getMyRegion()
            if(myRegionResponse !is ApiResponse.Success) return@launch
            var idx = 0
            for(regionData in myRegionResponse.data.data) {
                if(regionData.isDefault == true) _myLivingRegionText.value = formatRegionName(regionData.region)
                else {
                    interestRegionText[idx] = formatRegionName(regionData.region)
                    myInterestRegionIds.add(regionData.region.id)
                    idx += 1
                }
            }
            _myInterestRegionCount.value = idx
            _myInterestRegionTextList.value = interestRegionText
        }
    }

    val deleteInterestRegion: (idx: Int) -> Unit = { idx ->
        myInterestRegionIds.removeAt(idx)
        val interestRegionTextList = _myInterestRegionTextList.value!!
        for (i in idx until MAX_ADDITIONAL_COUNT - 1) {
            interestRegionTextList[i] = interestRegionTextList[i + 1]
        }
        interestRegionTextList[MAX_ADDITIONAL_COUNT - 1] = ""
        _myInterestRegionTextList.value = interestRegionTextList
        _myInterestRegionCount.value = _myInterestRegionCount.value!! - 1

    }

    private fun formatRegionName(region: RegionInterest): String {
        return if(region.parent == null) "${region.name} 전체"
        else "${region.parent.name} ${region.name}"
    }
}