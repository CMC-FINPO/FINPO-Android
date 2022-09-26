package com.finpo.app.ui.community.detail.image_viewer

import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.PagerSnapHelper
import com.finpo.app.R
import com.finpo.app.databinding.FragmentImageViewerBinding
import com.finpo.app.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageViewerFragment : BaseFragment<FragmentImageViewerBinding>(R.layout.fragment_image_viewer) {
    @Inject lateinit var imageAdapter: ImageViewerAdapter
    private val args by navArgs<ImageViewerFragmentArgs>()

    override fun doViewCreated() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvImage)

        binding.rvImage.adapter = imageAdapter
        imageAdapter.submitList(args.imgs.toList())

        binding.rvImage.scrollToPosition(args.startIndex)
    }
}