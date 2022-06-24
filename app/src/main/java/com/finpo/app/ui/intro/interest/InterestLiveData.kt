package com.finpo.app.ui.intro.interest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.local.CategoryId
import com.finpo.app.model.remote.ParentCategory
import com.finpo.app.model.remote.ParentCategoryResponse
import com.finpo.app.model.remote.Region
import com.finpo.app.model.remote.RegionResponse
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.MutableSingleLiveData
import com.finpo.app.utils.SingleLiveData
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class InterestLiveData@Inject constructor(
    private val introRepository: IntroRepository
) : ViewModel() {
    private val _categoryData = MutableLiveData<List<ParentCategory>>()
    val categoryData: LiveData<List<ParentCategory>> = _categoryData

    private val _userInterestData = MutableLiveData<MutableSet<CategoryId>>()
    val userInterestData: LiveData<MutableSet<CategoryId>> = _userInterestData

    init {
        _userInterestData.value = mutableSetOf()
        getParentCategoryData()
    }

    private fun getParentCategoryData() {
        viewModelScope.launch {
            val parentCategoryResponse = introRepository.getParentCategory()
            parentCategoryResponse.onSuccess {
                _categoryData.value = data.data
            }
        }
    }

    fun interestItemClick(id: Int) {
        val interestList = _categoryData.value
        val clickedItem = interestList?.indexOfFirst { it.id == id } ?: -1

        if(clickedItem == -1 || interestList == null)   return

        interestList[clickedItem].isChecked = !interestList[clickedItem].isChecked
        _categoryData.value = interestList!!

        if(CategoryId(id) in _userInterestData.value!!) _userInterestData.value!!.remove(CategoryId(id))
        else _userInterestData.value!!.add(CategoryId(id))
        _userInterestData.value = _userInterestData.value
    }
}