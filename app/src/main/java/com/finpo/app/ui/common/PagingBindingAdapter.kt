package com.finpo.app.ui.common

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finpo.app.utils.LinearLayoutManagerWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@BindingAdapter("doScrollBottom")
fun RecyclerView.infiniteScrolls(doScrollBottom: () -> Unit) {
    this.addOnScrollListener((object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1
            if (itemTotalCount == 0) return
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
            Log.d("infiniteScrolls", "스크롤 감지, $itemTotalCount $lastVisibleItemPosition")
            if (lastVisibleItemPosition == itemTotalCount) {
                Log.d("infiniteScrolls", "하단 감지, $itemTotalCount")
                doScrollBottom()
            }
        }
    }))
}