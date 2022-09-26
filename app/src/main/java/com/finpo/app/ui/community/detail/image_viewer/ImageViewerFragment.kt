package com.finpo.app.ui.community.detail.image_viewer

import androidx.navigation.fragment.navArgs
import com.finpo.app.R
import com.finpo.app.databinding.FragmentImageViewerBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageViewerFragment : BaseFragment<FragmentImageViewerBinding>(R.layout.fragment_image_viewer) {
    private val args by navArgs<ImageViewerFragmentArgs>()

    override fun doViewCreated() {

    }
}