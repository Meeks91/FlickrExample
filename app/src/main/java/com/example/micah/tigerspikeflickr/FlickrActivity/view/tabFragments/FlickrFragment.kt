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

/**
 * generic fragment to display flickr images
 */
class FlickrFragment: Fragment() {

    private lateinit var fragmentType: FragmentType
    private val TAG = this::class.simpleName
    private lateinit var flickrImagesAdapter: FlickrImagesAdapter

    companion object {

        /**
         * generates and returns an instance of the FlickrFragment
         * using the specified [flickrRVAdapter] and [type].
         * Note that the different tabs are the same fragment class
         * and their different layouts rely on the fragmentType of [flickrRVAdapter]
         */
        fun newInstance(flickrRVAdapter: FlickrImagesAdapter, type: FragmentType): FlickrFragment {

            //create FlickrFragment
            val detailsFragment = FlickrFragment()

            //populate fields:
            detailsFragment.setAdapter(flickrRVAdapter)
            detailsFragment.fragmentType = type

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

    //MARK: ------ INITIALISATION

    /**
     * binds the global [flickrImagesAdapter]
     * to the [imagesRV] and assigns the
     * [scrollListener] to enable infinite scrolling
     */
    private fun initImagesRV() {

        flickrImagesAdapter.bindTo(imagesRV)

        imagesRV.addOnScrollListener(scrollListener)
    }

    /**
     * assigns the specified [flickrImagesAdapter]
     * to the gobal [flickrImagesAdapter]
     */
    fun setAdapter(flickrRVAdapter: FlickrImagesAdapter) {

        this.flickrImagesAdapter = flickrRVAdapter
    }

    /**
     * lazily create an instance of EndlessRecyclerViewScrollListener.
     * It handles broacasting the event to request more images. The type
     * of images to retrieve is determined by the global [fragmentType]
     */
    private val scrollListener by lazy { object : EndlessRecyclerViewScrollListener(imagesRV.layoutManager as StaggeredGridLayoutManager) {

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

            //store which type of image to request more of
            val moreImagesRequestType = if (fragmentType == FragmentType.detailed) EventType.retrieveMoreDetailed

                                                else EventType.retrieveMoreCurrentTag

            RxBus.bus.onNext(BusEvent(moreImagesRequestType, fragmentType))
        }
    }}

    //MARK: ------ INITIALISATION
}