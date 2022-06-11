package com.finpo.app.ui.intro.living_area

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.Region
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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

    private val _regionDetailSelEvent = MutableSingleLiveData<Int>()
    val regionDetailSelEvent: SingleLiveData<Int> = _regionDetailSelEvent

    var regionName = ""

    init {
        getRegionAll()
    }

    private fun getRegionAll() {
        viewModelScope.launch {
            val data = introRepository.getRegionAll()
            if(data.isSuccessful && data.body() != null) {
                _regionData.value = data.body()
                _regionSel.value = data.body()!!.data[0].id
                _regionSelEvent.setValue(_regionSel.value!!)
            }
        }
    }

    private fun setRegionName() {
        regionName = _regionData.value?.data?.find { it.id == _regionSel.value }?.name ?: ""
    }

    fun selectRegion(regionId: Int) {
        if(regionId == _regionSel.value) return
        _regionSel.value = regionId
        _regionSelEvent.setValue(regionId)
    }

    fun getRegionDetail(regionId: Int) {
        viewModelScope.launch {
            val data = introRepository.getRegionDetail(regionId)
            if(data.isSuccessful && data.body() != null) {
                setRegionName()
                _regionDetailData.value = RegionResponse(listOf(Region(_regionSel.value!!,
                    "$regionName 전체"
                )) + data.body()!!.data)
                Log.d("region","${_regionDetailData.value}")
            }
        }
    }
}