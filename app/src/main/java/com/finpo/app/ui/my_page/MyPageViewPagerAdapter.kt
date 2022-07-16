package com.finpo.app.ui.my_page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finpo.app.ui.my_page.my_bookmark.MyBookmarkFragment
import com.finpo.app.ui.my_page.my_comment.MyCommentFragment
import com.finpo.app.ui.my_page.my_writing.MyWritingFragment

class MyPageViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MyWritingFragment()
            1 -> MyCommentFragment()
            else -> MyBookmarkFragment()
        }
    }
}