package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabFragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.FlickrImagesAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.TaggedImagesRxRvAdapter

class FlickrTabsAdapter(fm: FragmentManager,
                        private val titlesArray: Array<String>,
                        private val detailedImagesRxRVAdapter: FlickrImagesAdapter,
                        private val taggedImagesRxRvAdapter: TaggedImagesRxRvAdapter): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = when (position) {

        0 -> FlickrFragment.newInstance(detailedImagesRxRVAdapter, FragmentType.detailed)
        else -> FlickrFragment.newInstance(taggedImagesRxRvAdapter, FragmentType.tagged)
    }

    override fun getPageTitle(position: Int): CharSequence  = titlesArray[position]

    override fun getCount(): Int = 2
}