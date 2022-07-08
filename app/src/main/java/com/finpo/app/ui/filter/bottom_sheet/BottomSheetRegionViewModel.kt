package com.finpo.app.ui.filter.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.finpo.app.model.local.IdName
import com.finpo.app.ui.common.RegionViewModel
import com.finpo.app.utils.MAX_FILTER_REGION_COUNT
import javax.inject.Inject

class BottomSheetRegionViewModel @Inject constructor(
    val bottomFilterRegionViewModel: RegionViewModel
): ViewModel() {
    fun initEditRegionSelTextList(filterRegionSelTextList: LiveData<MutableList<String>>, regionIds: MutableList<Int?>) {
        bottomFilterRegionViewModel.MAX_COUNT = MAX_FILTER_REGION_COUNT
        bottomFilterRegionViewModel.setRegionTextList(filterRegionSelTextList.value?.toMutableList() ?: mutableListOf())
        bottomFilterRegionViewModel.regionIds = regionIds.toMutableList()
        bottomFilterRegionViewModel.setRegionCount(regionIds.toMutableList().size)
    }
}