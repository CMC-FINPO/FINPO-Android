package com.finpo.app.ui.community_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor() : ViewModel() {
    val editTextInput = MutableLiveData<String>()
}