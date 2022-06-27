package com.finpo.app.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.finpo.app.utils.MAX_FILTER_REGION_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(): ViewModel() {
    private val _filterRegionSelTextList = MutableLiveData<MutableList<String>>()
    val filterRegionSelTextList: LiveData<MutableList<String>> = _filterRegionSelTextList

    private val _filterRegionSelCount = MutableLiveData<Int>()
    val filterRegionSelCount: LiveData<Int> = _filterRegionSelCount

    init {
        _filterRegionSelTextList.value = MutableList(6){""}
        _filterRegionSelCount.value = 0
    }

    fun deleteFilterRegion(deleteIndex: Int) {
        val detailRegionTextList = _filterRegionSelTextList.value!!
//        additionalRegionDetailIdList.removeAt(deleteIndex)
//        Log.d("regionid","$additionalRegionDetailIdList")
        for(i in deleteIndex until MAX_FILTER_REGION_COUNT - 1) {
            detailRegionTextList[i] = detailRegionTextList[i + 1]
        }
        detailRegionTextList[MAX_FILTER_REGION_COUNT - 1] = ""
        _filterRegionSelTextList.value = detailRegionTextList
        _filterRegionSelCount.value = _filterRegionSelCount.value!! - 1
    }
}