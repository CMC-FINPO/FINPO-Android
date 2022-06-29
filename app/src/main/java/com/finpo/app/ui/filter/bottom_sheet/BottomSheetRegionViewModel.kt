package com.finpo.app.ui.filter.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.local.IdName
import com.finpo.app.model.remote.Region
import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.RegionRepository
import com.finpo.app.utils.MAX_FILTER_REGION_COUNT
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

class BottomSheetRegionViewModel @Inject constructor(
    private val regionRepository: RegionRepository
): ViewModel() {
    private val _editRegionSelTextList = MutableLiveData<MutableList<IdName>>()
    val editRegionSelTextList: LiveData<MutableList<IdName>> = _editRegionSelTextList

    private val _editRegionSelCount = MutableLiveData<Int>()
    val editRegionSelCount: LiveData<Int> = _editRegionSelCount

    private val _editRegionData = MutableLiveData<RegionResponse>()
    val editRegionData: LiveData<RegionResponse> = _editRegionData

    private val _editRegionSel = MutableLiveData<Int>()
    val editRegionSel: LiveData<Int> = _editRegionSel

    private val _editRegionDetailData = MutableLiveData<RegionResponse>()
    val editRegionDetailData: LiveData<RegionResponse> = _editRegionDetailData

    private val _chooseMaxToastEvent = MutableSingleLiveData<Boolean>()
    val chooseMaxToastEvent: SingleLiveData<Boolean> = _chooseMaxToastEvent

    private val _regionOverlapToastEvent = MutableSingleLiveData<Boolean>()
    val regionOverlapToastEvent: SingleLiveData<Boolean> = _regionOverlapToastEvent

    private val editRegionName = MutableLiveData<String>()
    val editRegionDetailIdList = mutableListOf<RegionRequest>()

    fun getRegionData() {
        viewModelScope.launch {
            val regionResponse = regionRepository.getRegionAll()
            regionResponse.onSuccess {
                _editRegionData.value = data!!
                getRegionDetailData(data.data[0].id)
            }
        }
    }

    fun getRegionDetailData(id: Int) {
        _editRegionSel.value = id
        editRegionName.value = _editRegionData.value?.data?.find { it.id == id }?.name ?: ""
        viewModelScope.launch {
            val regionDetailResponse = regionRepository.getRegionDetail(id)
            regionDetailResponse.onSuccess {
                _editRegionDetailData.value = RegionResponse(listOf(Region(id, "${editRegionName.value} 전체")) + data!!.data)
            }
        }
    }

    fun initEditRegionSelTextList(filterRegionSelTextList: LiveData<MutableList<IdName>>, filterRegionSelCount: LiveData<Int>) {
        _editRegionSelTextList.value = filterRegionSelTextList.value?.toMutableList()
        _editRegionSelCount.value = filterRegionSelCount.value
    }

    fun selectEditRegionDetail(editRegionDetailId: Int, editRegionDetailText: String) {
        if(_editRegionSelCount.value!! >= MAX_FILTER_REGION_COUNT) {
            _chooseMaxToastEvent.setValue(true)
            return
        }
        val regionDetailTextFormatted = if(editRegionDetailId == _editRegionSel.value!!)  editRegionDetailText
        else "${editRegionName.value!!} $editRegionDetailText"

        if(IdName(editRegionDetailId, regionDetailTextFormatted) in _editRegionSelTextList.value!!) {
            _regionOverlapToastEvent.setValue(true)
            return
        }

        _editRegionSelTextList.value!![_editRegionSelCount.value!!] = IdName(editRegionDetailId, regionDetailTextFormatted)
        _editRegionSelTextList.value = _editRegionSelTextList.value!!
        _editRegionSelCount.value = _editRegionSelCount.value!! + 1
    }

    fun deleteEditRegion(deleteIndex: Int) {
        val editRegionTextList = _editRegionSelTextList.value!!
        for (i in deleteIndex until MAX_FILTER_REGION_COUNT - 1) {
            editRegionTextList[i] = editRegionTextList[i + 1]
        }
        editRegionTextList[MAX_FILTER_REGION_COUNT - 1] = IdName(null, "")
        _editRegionSelTextList.value = editRegionTextList
        _editRegionSelCount.value = _editRegionSelCount.value!! - 1
    }
}