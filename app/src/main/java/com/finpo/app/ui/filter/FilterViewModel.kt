package com.finpo.app.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.CategoryChild
import com.finpo.app.model.remote.CategoryChildFormat
import com.finpo.app.model.remote.RegionRequest
import com.finpo.app.repository.FilterRepository
import com.finpo.app.utils.MAX_ADDITIONAL_COUNT
import com.finpo.app.utils.MAX_FILTER_REGION_COUNT
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterRepository: FilterRepository
): ViewModel() {
    private val _filterRegionSelTextList = MutableLiveData<MutableList<String>>()
    val filterRegionSelTextList: LiveData<MutableList<String>> = _filterRegionSelTextList

    private val _filterRegionSelCount = MutableLiveData<Int>()
    val filterRegionSelCount: LiveData<Int> = _filterRegionSelCount

    private val _filterCategoryData = MutableLiveData<List<CategoryChildFormat>>()
    val filterCategoryData: LiveData<List<CategoryChildFormat>> = _filterCategoryData

    init {
        getCategory()
        _filterRegionSelTextList.value = MutableList(6){""}
        _filterRegionSelCount.value = 0
    }

    private fun getCategory() {
        viewModelScope.launch {
            val filterResponse = filterRepository.getCategoryChildFormat()
            filterResponse.onSuccess {
                _filterCategoryData.value = data.data
            }
        }
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