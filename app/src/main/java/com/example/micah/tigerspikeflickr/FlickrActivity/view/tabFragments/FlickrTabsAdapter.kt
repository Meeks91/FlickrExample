package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabFragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.FlickrImagesAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.TaggedImagesRxRvAdapter

/**
 * tab adapter for the FlickrFragments
 */
class FlickrTabsAdapter(fm: FragmentManager,
                        private val titlesArray: Array<String>,
                        private val detailedImagesRxRVAdapter: FlickrImagesAdapter,
                        private val taggedImagesRxRvAdapter: TaggedImagesRxRvAdapter): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = when (position) {

        //note: the same fragment class is created but their adapter
        //and FragmentType makes makes their layout different
        0 -> FlickrFragment.newInstance(detailedImagesRxRVAdapter, FragmentType.detailed)
        else -> FlickrFragment.newInstance(taggedImagesRxRvAdapter, FragmentType.tagged)
    }

    override fun getPageTitle(position: Int): CharSequence  = titlesArray[position]

    override fun getCount(): Int = 2
}