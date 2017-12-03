package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.EndlessRecyclerViewScrollListener
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.FlickrImagesAdapter
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.BusEvent
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.EventType
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.RxBus
import com.example.micah.tigerspikeflickr.R
import kotlinx.android.synthetic.main.generic_flickr_tab.*

class FlickrFragment: Fragment() {

    private lateinit var type: FragmentType
    private val TAG = this::class.simpleName
    private lateinit var adapter: FlickrImagesAdapter

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        fun newInstance(flickrRVAdapter: FlickrImagesAdapter, type: FragmentType): FlickrFragment {

            //create FlickrFragment
            val detailsFragment = FlickrFragment()

            //populate fields:
            detailsFragment.setAdapter(flickrRVAdapter)
            detailsFragment.type = type

            return detailsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.generic_flickr_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImagesRV()
    }

    private fun initImagesRV() {

        adapter.bindTo(imagesRV)

        imagesRV.addOnScrollListener(scrollListener)
    }

    fun setAdapter(flickrRVAdapter: FlickrImagesAdapter) {

        this.adapter = flickrRVAdapter
    }

    private val scrollListener by lazy { object : EndlessRecyclerViewScrollListener(imagesRV.layoutManager as StaggeredGridLayoutManager) {

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

            //store which type of image to request more of
            val moreImagesRequestType = if (type == FragmentType.detailed) EventType.retrieveMoreDetailed else EventType.retrieveMoreCurrentTag

            RxBus.bus.onNext(BusEvent(moreImagesRequestType, type))
        }
    }}
}