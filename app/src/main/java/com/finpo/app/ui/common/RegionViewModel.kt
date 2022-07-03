package com.finpo.app.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.Region
import com.finpo.app.model.remote.RegionInterest
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.MyInfoRepository
import com.finpo.app.repository.RegionRepository
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionViewModel @Inject constructor(
    private val regionRepository: RegionRepository
) : ViewModel() {
    var MAX_COUNT = 0

    private val _regionTextList = MutableLiveData<MutableList<String>>()
    val regionTextList: LiveData<MutableList<String>> = _regionTextList

    val regionIds: MutableList<Int?> = mutableListOf()

    val _regionCount = MutableLiveData<Int>()
    val regionCount: LiveData<Int> = _regionCount

    private val _regionData = MutableLiveData<RegionResponse>()
    val regionData: LiveData<RegionResponse> = _regionData

    private val _regionDetailData = MutableLiveData<RegionResponse>()
    val regionDetailData: LiveData<RegionResponse> = _regionDetailData

    private val _regionSel = MutableLiveData<Int>()
    val regionSel: LiveData<Int> = _regionSel

    private val _chooseMaxToastEvent = MutableSingleLiveData<Boolean>()
    val chooseMaxToastEvent: SingleLiveData<Boolean> = _chooseMaxToastEvent

    private val _regionOverlapToastEvent = MutableSingleLiveData<Boolean>()
    val regionOverlapToastEvent: SingleLiveData<Boolean> = _regionOverlapToastEvent

    private val regionName = MutableLiveData<String>()

    fun setRegionTextList(textList: MutableList<String>) {
        _regionTextList.value = textList
    }

    fun setRegionCount(count: Int) {
        _regionCount.value = count
    }

    fun getRegionDataByRemote() {
        viewModelScope.launch {
            val regionResponse = regionRepository.getRegionAll()
            regionResponse.onSuccess {
                _regionData.value = data!!
                getRegionDetailData(data.data[0].id)
            }
        }
    }

    fun getRegionDataByLocal(data: RegionResponse) {
        _regionData.value = data
        getRegionDetailData(data.data[0].id)
    }

    fun getRegionDetailData(id: Int) {
        _regionSel.value = id
        regionName.value = _regionData.value?.data?.find { it.id == id }?.name ?: ""
        viewModelScope.launch {
            val regionDetailResponse = regionRepository.getRegionDetail(id)
            regionDetailResponse.onSuccess {
                _regionDetailData.value = RegionResponse(listOf(Region(id, "${regionName.value} 전체")) + data!!.data)
            }
        }
    }

    fun selectEditRegionDetail(editRegionDetailId: Int, editRegionDetailText: String) {
        if(_regionCount.value!! >= MAX_ADDITIONAL_COUNT) {
            _chooseMaxToastEvent.setValue(true)
            return
        }
        val regionDetailTextFormatted = if(editRegionDetailId == _regionSel.value!!)  editRegionDetailText
        else "${regionName.value!!} $editRegionDetailText"

        if(regionDetailTextFormatted in _regionTextList.value!!) {
            _regionOverlapToastEvent.setValue(true)
            return
        }

        regionIds.add(editRegionDetailId)

        _regionTextList.value!![_regionCount.value!!] = regionDetailTextFormatted
        _regionTextList.value = _regionTextList.value!!
        _regionCount.value = _regionCount.value!! + 1
    }

    val deleteRegion: (idx: Int) -> Unit = { idx ->
        regionIds.removeAt(idx)
        val regionTextList = _regionTextList.value!!
        for (i in idx until MAX_ADDITIONAL_COUNT - 1) {
            regionTextList[i] = regionTextList[i + 1]
        }
        regionTextList[MAX_ADDITIONAL_COUNT - 1] = ""
        _regionTextList.value = regionTextList
        _regionCount.value = _regionCount.value!! - 1
    }

    fun formatRegionName(region: RegionInterest): String {
        return if(region.parent == null) "${region.name} 전체"
        else "${region.parent.name} ${region.name}"
    }
}