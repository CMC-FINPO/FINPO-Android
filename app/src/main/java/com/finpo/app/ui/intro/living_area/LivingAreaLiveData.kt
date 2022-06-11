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

    private val _regionSelEvent = MutableSingleLiveData<Boolean>()
    val regionSelEvent: SingleLiveData<Boolean> = _regionSelEvent

    init {
        getRegionAll()
    }

    private fun getRegionAll() {
        viewModelScope.launch {
            val data = introRepository.getRegionAll()
            if(data.isSuccessful && data.body() != null) {
                _regionData.value = data.body()
                _regionSel.value = data.body()!!.data[0].id
            }
        }
    }

    fun selectRegion(regionId: Int) {
        _regionSel.value = regionId
        _regionSelEvent.setValue(true)
    }
}