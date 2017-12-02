package com.example.micah.tigerspikeflickr.FlickrActivity.view.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.EventType
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.DetailedImagesRxRvAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.FlickrFragment
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabs.FlickrImagesAdapter
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
                                DetailedImagesRxRvAdapter(presenter.imagesRxArrayList, compositeDisposable))
    }

    /**
     * assigns tabs to the layout and
     * sets it up with the flickrVP
     */
    private fun initTabLayout() {

        //assign adapter & flickrVP:
        tabLayout.setupWithViewPager(flickrVP)
        flickrVP.adapter = flickrTabsAdapter
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

    override fun loadUntagged(flickrModelsArrayList: ArrayList<FlickrModel>, fragmentTypes: FragmentType) {

//        if (fragmentTypes == FragmentType.detailed)
//
//            detailedImagesAdapter.add(flickrModelsArrayList)
    }

    //MARK: --------- PRESENTER MESSAGES
}

enum class FragmentType {

    detailed, normal
}

class FlickrPresenter(private val flickrApiHelper: FlickrApiHelper, private val delegate: FlickrActivityDelegate, private val disposable: CompositeDisposable) {

    var imagesRxArrayList = RxRecyclerViewArrayList<FlickrModel>()

    init {

        onRetrieveUnTaggedImages()

        initRxBusSubscription()
    }

    private fun initRxBusSubscription() {

        RxBus.bus.subscribe{

            when (it.type) {

                EventType.retrieveMoreDetailed -> onRetrieveUnTaggedImages()
                else -> onRetrieveUnTaggedImages()
            }

        }.addTo(disposable)
    }

    private fun onRetrieveUnTaggedImages() {

        flickrApiHelper
                .getAllImages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUntaggedImagesRetrieved, this::onImageRetrievalFailure)
    }

    private fun onUntaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        imagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)
    }

    private fun onImageRetrievalFailure(throwable: Throwable){

         delegate.displayAlert(throwable.localizedMessage)
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
}

class FlickrApiHelper {

    private val flickrModelType = object: TypeToken<FlickrApiResponse>(){}.type

    fun getAllImages(): Single<FlickrApiResponse> =

        ApiHelper.request<FlickrApiResponse>(flickrModelType, "")

    fun getImagesWith(tag: String): Single<FlickrApiResponse> =

            ApiHelper.request<FlickrApiResponse>(flickrModelType, "?tag=$tag")
}

interface FlickrActivityDelegate {
    fun loadUntagged(flickrModelsArrayList: ArrayList<FlickrModel>, fragmentTypes: FragmentType)
    fun displayAlert(message: String)
}

class FlickrTabsAdapter(fm: FragmentManager, private val titlesArray: Array<String>, private val flickrRVAdapter: FlickrImagesAdapter): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = when (position) {

        1 -> FlickrFragment.newInstance(flickrRVAdapter, FragmentType.detailed)
        else -> FlickrFragment.newInstance(flickrRVAdapter, FragmentType.normal)
    }

    override fun getPageTitle(position: Int): CharSequence  = titlesArray[position]

    override fun getCount(): Int = 2
}
