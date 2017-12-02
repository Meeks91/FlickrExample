package com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrModel
import com.example.micah.tigerspikeflickr.R
import kotlinx.android.synthetic.main.generic_flickr_tab.*

class DetailsFragmentTab<VH : RecyclerView.ViewHolder> : FlickrFragment<VH>() {

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment DetailsFragmentTab.
         */

        fun<VH : RecyclerView.ViewHolder> newInstance(flickrRVAdapter: FlickrRVAdapter<VH>): DetailsFragmentTab<VH> {

            val detailsFragment = DetailsFragmentTab<VH>()

            detailsFragment.setAdapter(flickrRVAdapter)

            return detailsFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    //MARK: INITIALISATION

    private fun init(){

    }

    //MARK: INITIALISATION

    override fun onDetach() {
        super.onDetach()
    }
}

open class FlickrFragment<VH : RecyclerView.ViewHolder>: Fragment() {

    private lateinit var adapter: FlickrRVAdapter<VH>

    fun setAdapter(flickrRVAdapter: FlickrRVAdapter<VH>) {

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

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        imagesRV.layoutManager = staggeredGridLayoutManager

        imagesRV.adapter = adapter
    }
}

class DetailedImagesRVAdapter(flickrModelsArrayList: ArrayList<FlickrModel>): FlickrRVAdapter<DetailedImagesRVViewHolder>(flickrModelsArrayList) {

    private val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedImagesRVViewHolder {

        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.details_image_item, parent, false)

        return DetailedImagesRVViewHolder(itemView)
    }

    override fun getItemCount(): Int = flickrImagesArrayList.size

    override fun onBindViewHolder(holder: DetailedImagesRVViewHolder, position: Int) {

        val flickrModel = flickrImagesArrayList[position]

        Glide.with(holder.itemView.context).load(flickrModel.imageUrl).into(holder.flickrImageIV)

        d(TAG, "Tags: ${flickrModel.tags}" )

//        holder.dateTV.text = flickrModel.date
        holder.tagsTV.text = "Tags: ${flickrModel.tags}"
        holder.titleTV.text ="Title: ${flickrModel.title}"
    }
}

abstract class FlickrRVAdapter<VH : RecyclerView.ViewHolder>(var flickrImagesArrayList: ArrayList<FlickrModel>): RecyclerView.Adapter<VH>(){

    /**
     * adds the [flickrImagesArrayList] to the global
     * flickrImagesArrayList and reloads the adapter
     */
    fun add(flickrImagesArrayList: ArrayList<FlickrModel>) {

        val insertPosition = this.flickrImagesArrayList.size - 1

        this.flickrImagesArrayList.addAll(flickrImagesArrayList)

        this.notifyItemRangeInserted(insertPosition, this.flickrImagesArrayList.size - 1)
    }
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