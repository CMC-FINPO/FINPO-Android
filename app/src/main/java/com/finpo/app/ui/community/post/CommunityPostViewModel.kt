package com.finpo.app.ui.community.post

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finpo.app.model.remote.PostWritingRequest
import com.finpo.app.repository.CommunityRepository
import com.finpo.app.utils.*
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    var id = -1

    val editTextInput = MutableLiveData<String>()

    private val _backEvent = MutableSingleLiveData<Boolean>()
    val backEvent: SingleLiveData<Boolean> = _backEvent

    private val _showPreparationToastEvent = MutableSingleLiveData<Boolean>()
    val showPreparationToastEvent: SingleLiveData<Boolean> = _showPreparationToastEvent

    private val _goToCommunityHomeFragmentEvent = MutableSingleLiveData<Boolean>()
    val goToCommunityHomeFragmentEvent: SingleLiveData<Boolean> = _goToCommunityHomeFragmentEvent

    private val _finishButtonClickEvent = MutableSingleLiveData<Boolean>()
    val finishButtonClickEvent: SingleLiveData<Boolean> = _finishButtonClickEvent

    private val _selectedUriList = MutableLiveData<List<Uri>>(emptyList())
    val selectedUriList: LiveData<List<Uri>> = _selectedUriList

    fun updateUriList(uris: List<Uri>) {
        _selectedUriList.value = uris
    }

    fun deleteUriListItem(uri: Uri) {
        val tempUriList = _selectedUriList.value!!.toMutableList()
        tempUriList.remove(uri)
        _selectedUriList.value = tempUriList
    }

    fun showPreparationToast() {
        _showPreparationToastEvent.setValue(true)
    }

    fun backClick() {
        _backEvent.setValue(true)
    }

    fun finishClick() {
        if(editTextInput.value.isNullOrEmpty()) return
        _finishButtonClickEvent.setValue(true)
    }

    fun postOrPutWriting(bitmapList: List<Bitmap?>) = viewModelScope.launch {
        val imgs = uploadImage(bitmapList)?.addOrder()
        val response = if(id == -1) communityRepository.postWriting(PostWritingRequest(content = editTextInput.value ?: "", imgs = imgs))
        else communityRepository.putWriting(id, PostWritingRequest(content = editTextInput.value ?: "", imgs = imgs))
        response.onSuccess {
            _goToCommunityHomeFragmentEvent.setValue(id == -1)
        }
    }

    private suspend fun uploadImage(bitmapList: List<Bitmap?>) =
        withContext(viewModelScope.coroutineContext) {
            val bitmapMultipartBodyList: List<MultipartBody.Part?> =
                ImageUtils().getMultipartBodyImgListFromBitmapList(bitmapList)
            val response = communityRepository.uploadCommunityImages(bitmapMultipartBodyList)
            response.getOrNull()?.data?.imgUrls
        }
}