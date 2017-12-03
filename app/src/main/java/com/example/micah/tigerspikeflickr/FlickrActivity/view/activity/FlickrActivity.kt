package com.example.micah.tigerspikeflickr.FlickrActivity.view.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.EventType
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.DetailedImagesRxRvAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.FlickrFragment
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.FlickrImagesAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.TaggedImagesRxRvAdapter
import com.example.micah.tigerspikeflickr.GlobalModels.api.ApiHelper
import com.example.micah.tigerspikeflickr.R
import com.example.micah.tigerspikeflickr.RxBus
import com.example.micah.tigerspikeflickr.dagger.DaggerInjector
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class FlickrActivity : AppCompatActivity(), FlickrActivityDelegate {


    @Inject lateinit var presenter: FlickrPresenter
    private lateinit var flickrTabsAdapter: FlickrTabsAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDaggerInjection()

        initRVAdapters()

        initFlickrTabsAdapter()

        initTabLayout()
    }

    private fun initRVAdapters() {

//        detailedImagesAdapter = DetailedImagesRVAdapter(ArrayList<FlickrModel>())
    }

    //MARK: -------- INITIALISATION

    /**
     * inits the global [flickrTabsAdapter]
     */
    private fun initFlickrTabsAdapter() {

        val tabsTitlesArray = arrayOf(getString(R.string.details_tab), getString(R.string.more_of_tag))

        flickrTabsAdapter = FlickrTabsAdapter(
                                supportFragmentManager,
                                tabsTitlesArray ,
                                DetailedImagesRxRvAdapter(presenter.imagesRxArrayList, compositeDisposable),
                                TaggedImagesRxRvAdapter(presenter.taggedImagesRxArrayList, compositeDisposable))
    }

    /**
     * assigns tabs to the layout and
     * sets it up with the flickrVP
     */
    private fun initTabLayout() {

        //assign adapter & flickrVP:
        tabLayout.setupWithViewPager(flickrVP)
        flickrVP.adapter = flickrTabsAdapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            //unused
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            //unused
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab?.position == 1)

                      presenter.onTaggedImageTabSelected()
            }
        })
    }

    /**
     * inits the dagger injection
     * for this activity
     */
    private fun initDaggerInjection() {

        DaggerInjector.configureInjectionFor(this, compositeDisposable).inject(this)
    }

    //MARK: -------- INITIALISATION

    //MARK: --------- PRESENTER MESSAGES

    override fun displayAlert(message: String) {

        Snackbar.make(window.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

//    override fun loadUntagged(flickrModelsArrayList: ArrayList<FlickrModel>, fragmentTypes: FragmentType) {

//        if (fragmentTypes == FragmentType.detailed)
//
//            detailedImagesAdapter.add(flickrModelsArrayList)
//    }

    override fun selectTabAt(index: Int) {

        tabLayout.getTabAt(index)?.select()
    }

    //MARK: --------- PRESENTER MESSAGES
}

enum class FragmentType {

    detailed, tagged
}

class FlickrPresenter(private val flickrApiHelper: FlickrApiHelper, private val delegate: FlickrActivityDelegate, private val disposable: CompositeDisposable) {

    var imagesRxArrayList = RxRecyclerViewArrayList<FlickrModel>()
    var taggedImagesRxArrayList = RxRecyclerViewArrayList<FlickrModel>()
    lateinit var currentSearchTag: String

    init {

        onRequestDetailedImages()

        initRxBusSubscription()
    }

    private fun initRxBusSubscription() {

        RxBus.bus.subscribe{

            when (it.type) {

                EventType.retrieveMoreDetailed -> onRequestDetailedImages()
                EventType.retrieveNewTag -> onRequestToRetrieveTaggedUsing(it.data as Int)
                EventType.retrieveMoreCurrentTag -> onRequestToRetrieveMoreCurrentTag()
            }

        }.addTo(disposable)
    }

    private fun onRequestDetailedImages() {

        flickrApiHelper
                .getAllImages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUntaggedImagesRetrieved, this::onImageRetrievalFailure)
    }

    private fun onRequestToRetrieveTaggedUsing(index: Int){

        //unwrap searchTag
        val searchTag = imagesRxArrayList[index].searchTag.takeIf { it != ""}

                                    //short circuit & notify user we can't find other images
                                    ?: return delegate.displayAlert("No Tag available :(")

        //store currentSearchTag
        currentSearchTag = searchTag

        //execute update:
        flickrApiHelper
                .getImagesWith(searchTag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNewTaggedImagesRetrieved, this::onImageRetrievalFailure)
    }

    private fun onRequestToRetrieveMoreCurrentTag(){

        flickrApiHelper
                .getImagesWith(currentSearchTag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMoreTaggedImagesRetrieved, this::onImageRetrievalFailure)
    }

    private fun onUntaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        imagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)
    }

    private fun onNewTaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        //check if there are any tagged images
        if (flickrApiResponse.flickrModelsArrayList.size > 0) {

            //update tagged images:
            taggedImagesRxArrayList.clear()
            taggedImagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)

            //go to tagged images tab
            delegate.selectTabAt(1)
        }

        else

            delegate.displayAlert("No matching tags found :(")
    }

    private fun onMoreTaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        //check if there are any tagged images
        if (flickrApiResponse.flickrModelsArrayList.size > 0)

            taggedImagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)

        else

            delegate.displayAlert("No new images found :(")
    }



    private fun onImageRetrievalFailure(throwable: Throwable){

         delegate.displayAlert(throwable.localizedMessage)
      }

    fun onTaggedImageTabSelected() {

        if (taggedImagesRxArrayList.size == 0)

            delegate.displayAlert("No tagged images to show :(")
    }
}

data class FlickrApiResponse(@SerializedName("items") val flickrModelsArrayList: RxRecyclerViewArrayList<FlickrModel>)

data class FlickrModel(val title: String,
                       val link: String,
                       private val author: String,
                       private @SerializedName("date_taken") val dateTaken: String,
                       private @SerializedName("tags") val rawTags: String,
                       private val media: FlickrMedia){

    data class FlickrMedia (@SerializedName("m") val imageUrl: String)

    val imageUrl get() =  media.imageUrl
    val tags get() = rawTags.takeIf {it.isNotEmpty()} ?: "Default tags"
    val searchTag get() = rawTags.substringBefore(" ", "")
}

class FlickrApiHelper {

    private val flickrModelType = object: TypeToken<FlickrApiResponse>(){}.type

    fun getAllImages(): Single<FlickrApiResponse> =

        ApiHelper.request<FlickrApiResponse>(flickrModelType, "")

    fun getImagesWith(tag: String): Single<FlickrApiResponse> =

            ApiHelper.request<FlickrApiResponse>(flickrModelType, "?tag=$tag")
}

interface FlickrActivityDelegate {
    fun selectTabAt(index: Int)
    fun displayAlert(message: String)
}

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