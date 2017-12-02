package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.micah.rxRecyclerViewArrayListAdaper.LayoutConfig
import com.example.micah.rxRecyclerViewArrayListAdaper.Orientation
import com.example.micah.rxRecyclerViewArrayListAdaper.RowType
import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.*
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrModel
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FragmentType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.generic_flickr_tab.*



open class FlickrFragment: Fragment() {

    private lateinit var type: FragmentType
    private val TAG = this::class.simpleName

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment DetailsFragmentTab.
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

    private lateinit var adapter: FlickrImagesAdapter

    fun setAdapter(flickrRVAdapter: FlickrImagesAdapter) {

        this.adapter = flickrRVAdapter
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

    private val scrollListener by lazy { object : EndlessRecyclerViewScrollListener(imagesRV.layoutManager as StaggeredGridLayoutManager) {

        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

            RxBus.bus.onNext(BusEvent(EventType.retrieveMoreDetailed, type))
        }
    }}
}

class DetailedImagesRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.titleTV) lateinit var titleTV: TextView
    @BindView(R.id.dateTV) lateinit var dateTV: TextView
    @BindView(R.id.tagsTV) lateinit var tagsTV: TextView
    @BindView(R.id.flickrImageIV) lateinit var flickrImageIV: ImageView

    init {

        ButterKnife.bind(this, itemView)
    }
}

 abstract class FlickrImagesAdapter(private val compositeDisposable: CompositeDisposable) {

    abstract fun bindTo(imagesRV: RecyclerView)
}

class DetailedImagesRxRvAdapter(val imagesRxArrayList: RxRecyclerViewArrayList<FlickrModel>, private val compositeDisposable: CompositeDisposable): FlickrImagesAdapter(compositeDisposable){

    override fun bindTo(imagesRV: RecyclerView) {

        imagesRxArrayList.bind<DetailedImagesRVViewHolder>(imagesRV, R.layout.details_image_item, LayoutConfig(Orientation.vertical, RowType.staggered, 2)) { vh, imageModel ->

            Glide.with(vh.itemView.context).load(imageModel.imageUrl).into(vh.flickrImageIV)

            vh.tagsTV.text = "Tags: ${imageModel.tags}"
            vh.titleTV.text ="Title: ${imageModel.title}"

        }.addTo(compositeDisposable)
    }
}

class TaggedImagesRxRvAdapter(val imagesRxArrayList: RxRecyclerViewArrayList<FlickrModel>, private val compositeDisposable: CompositeDisposable): FlickrImagesAdapter(compositeDisposable){

    override fun bindTo(imagesRV: RecyclerView) {

        imagesRxArrayList.bind<DetailedImagesRVViewHolder>(imagesRV, R.layout.details_image_item, LayoutConfig(Orientation.vertical, RowType.staggered, 2)) { vh, imageModel ->

            Glide.with(vh.itemView.context).load(imageModel.imageUrl).into(vh.flickrImageIV)

        }.addTo(compositeDisposable)
    }
}