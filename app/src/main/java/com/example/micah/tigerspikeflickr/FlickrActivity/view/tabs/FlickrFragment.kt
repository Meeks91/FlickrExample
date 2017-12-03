package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.micah.rxRecyclerViewArrayListAdaper.LayoutConfig
import com.example.micah.rxRecyclerViewArrayListAdaper.Orientation
import com.example.micah.rxRecyclerViewArrayListAdaper.RowType
import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.BusEvent
import com.example.micah.tigerspikeflickr.EventType
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrModel
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FragmentType
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.EndlessRecyclerViewScrollListener
import com.example.micah.tigerspikeflickr.R
import com.example.micah.tigerspikeflickr.RxBus
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

            //store which type of image to request more of
            val moreImagesRequestType = if (type == FragmentType.detailed) EventType.retrieveMoreDetailed else EventType.retrieveMoreCurrentTag

            RxBus.bus.onNext(BusEvent(moreImagesRequestType, type))
        }
    }}
}

class DetailedImageRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.titleTV) lateinit var titleTV: TextView
    @BindView(R.id.tagsTV) lateinit var tagsTV: TextView
    @BindView(R.id.flickrImageIV) lateinit var flickrImageIV: ImageView
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar

    init {

        ButterKnife.bind(this, itemView)
    }

    /**
     *
     */
    @OnClick((R.id.seeMoreButton))
     fun onClick(view: View) {

        //broadcast event to retrieve more images with the flickrImageIV's current model's tag
        RxBus.bus.onNext(BusEvent((EventType.retrieveNewTag), adapterPosition))
    }
}

class TaggedImageRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    @BindView(R.id.flickrImageIV) lateinit var flickrImageIV: ImageView
    @BindView(R.id.progressBar) lateinit var progressBar: ProgressBar

    init {

        ButterKnife.bind(this, itemView)
    }
}

 abstract class FlickrImagesAdapter(private val compositeDisposable: CompositeDisposable) {

   lateinit var glideOptions: RequestOptions

     init {

         initGlideOpitons()
     }

     private fun initGlideOpitons(){

         glideOptions = RequestOptions()
         glideOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
     }

    abstract fun bindTo(imagesRV: RecyclerView)
}

class DetailedImagesRxRvAdapter(private val imagesRxArrayList: RxRecyclerViewArrayList<FlickrModel>, private val compositeDisposable: CompositeDisposable): FlickrImagesAdapter(compositeDisposable){

    override fun bindTo(imagesRV: RecyclerView) {

        imagesRxArrayList.bind<DetailedImageRVViewHolder>(imagesRV, R.layout.detailed_image_item, LayoutConfig(Orientation.vertical, RowType.staggered, 2)) { vh, imageModel ->

            Glide.with(vh.itemView.context).load(imageModel.imageUrl).apply(glideOptions).listener(object: RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    vh.progressBar.visibility = View.GONE

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    vh.progressBar.visibility = View.GONE

                    return false
                }
            }).into(vh.flickrImageIV)

            vh.tagsTV.text = "Tags: ${imageModel.tags}"
            vh.titleTV.text ="Title: ${imageModel.title}"

        }.addTo(compositeDisposable)
    }
}


class TaggedImagesRxRvAdapter(private val imagesRxArrayList: RxRecyclerViewArrayList<FlickrModel>, private val compositeDisposable: CompositeDisposable): FlickrImagesAdapter(compositeDisposable){

    override fun bindTo(imagesRV: RecyclerView) {

        imagesRxArrayList.bind<TaggedImageRVViewHolder>(imagesRV, R.layout.tagged_image_item, LayoutConfig(Orientation.vertical, RowType.staggered, 1)) { vh, imageModel ->

            Glide.with(vh.itemView.context).load(imageModel.imageUrl).apply(glideOptions).listener(object: RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    vh.progressBar.visibility = View.GONE

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    vh.progressBar.visibility = View.GONE

                    return false
                }
            }).into(vh.flickrImageIV)

        }.addTo(compositeDisposable)
    }
}