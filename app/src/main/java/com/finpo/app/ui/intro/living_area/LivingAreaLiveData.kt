package com.finpo.app.ui.intro.living_area

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.Region
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class LivingAreaLiveData@Inject constructor(
    private val introRepository: IntroRepository
) : ViewModel() {
    private val _regionData = MutableLiveData<RegionResponse>()
    val regionData: LiveData<RegionResponse> = _regionData

    private val _regionSel = MutableLiveData<Int>()
    val regionSel: LiveData<Int> = _regionSel

    private val _regionSelEvent = MutableSingleLiveData<Int>()
    val regionSelEvent: SingleLiveData<Int> = _regionSelEvent

    private val _regionDetailData = MutableLiveData<RegionResponse>()
    val regionDetailData: LiveData<RegionResponse> = _regionDetailData

    private val _regionDetailSel = MutableLiveData<Int>()
    val regionDetailSel: LiveData<Int> = _regionDetailSel

    private val _regionDetailText = MutableLiveData<String>()
    val regionDetailText: LiveData<String> = _regionDetailText

    private val _showRegionToastEvent = MutableSingleLiveData<Boolean>()
    val showRegionToastEvent: SingleLiveData<Boolean> = _showRegionToastEvent

    private val regionName = MutableLiveData<String>()

    init {
        regionName.value = ""
        _regionDetailText.value = ""
        getRegionAll()
    }

    private fun getRegionAll() {
        viewModelScope.launch {
            val getRegionAllResponse = introRepository.getRegionAll()
            if(getRegionAllResponse is ApiResponse.Success) {
                _regionData.value = getRegionAllResponse.data!!
                setRegion(getRegionAllResponse.data.data[0].id)
            }
        }
    }

    private fun setRegion(regionId: Int) {
        _regionSel.value = regionId
        _regionSelEvent.setValue(regionId)
        setRegionName(regionName, _regionSel.value!!)
        getRegionDetail(regionId, regionName, _regionDetailData)
    }

    fun selectRegion(regionId: Int) {
        if(regionId == _regionSel.value) return
        setRegion(regionId)
    }

    fun setRegionName(regionName: MutableLiveData<String> ,regionId: Int) {
        regionName.value = _regionData.value?.data?.find { it.id == regionId }?.name ?: ""
    }

    fun getRegionDetail(regionId: Int, nowRegion: MutableLiveData<String>,mutableLiveData: MutableLiveData<RegionResponse>) {
        viewModelScope.launch {
            val getRegionDetailResponse = introRepository.getRegionDetail(regionId)
            if(getRegionDetailResponse is ApiResponse.Success) {
                mutableLiveData.value = RegionResponse(listOf(Region(regionId,
                    "${nowRegion.value} 전체"
                )) + getRegionDetailResponse.data.data)
            }
        }
    }

    fun selectRegionDetail(regionDetailId: Int, regionDetailText: String) {
        if(_regionDetailText.value == regionDetailText)    return
        if(!_regionDetailText.value.isNullOrEmpty()) {
            _showRegionToastEvent.setValue(true)
            return
        }

        _regionDetailSel.value = regionDetailId

        _regionDetailText.value = if(_regionDetailSel.value == _regionSel.value) regionDetailText
        else "${regionName.value!!} $regionDetailText"
    }

    fun removeRegionDetail() {
        _regionDetailText.value = ""
    }
}