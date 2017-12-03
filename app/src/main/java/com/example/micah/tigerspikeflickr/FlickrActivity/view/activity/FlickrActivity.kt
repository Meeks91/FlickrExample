package com.example.micah.tigerspikeflickr.FlickrActivity.view.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.example.micah.tigerspikeflickr.FlickrActivity.FlickrPresenter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.DetailedImagesRxRvAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.recylerView.adapters.TaggedImagesRxRvAdapter
import com.example.micah.tigerspikeflickr.FlickrActivity.view.tabFragments.FlickrTabsAdapter
import com.example.micah.tigerspikeflickr.R
import com.example.micah.tigerspikeflickr.dagger.DaggerInjector
import io.reactivex.disposables.CompositeDisposable
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

        initFlickrTabsAdapter()

        initTabLayout()
    }


    //MARK: -------- INITIALISATION

    /**
     * inits the dagger injection
     * for this activity
     */
    private fun initDaggerInjection() {

        DaggerInjector.configureInjectionFor(this, compositeDisposable).inject(this)
    }

    /**
     * inits the global [flickrTabsAdapter]
     */
    private fun initFlickrTabsAdapter() {

        val tabsTitlesArray = arrayOf(getString(R.string.details_tab), getString(R.string.more_of_tag))

        flickrTabsAdapter = FlickrTabsAdapter(
                supportFragmentManager,
                tabsTitlesArray,
                DetailedImagesRxRvAdapter(presenter.imagesRxArrayList, compositeDisposable),
                TaggedImagesRxRvAdapter(presenter.taggedImagesRxArrayList, compositeDisposable))
    }

    /**
     * does core setup of the tabLayout
     * and assigns a tabSelectedListener
     */
    private fun initTabLayout() {

        //assign adapter & flickrVP:
        tabLayout.setupWithViewPager(flickrVP)
        flickrVP.adapter = flickrTabsAdapter

        tabLayout.addOnTabSelectedListener(generateTabSelectedListener())
    }

    /**
     * generates and returns a TabLayout.OnTabSelectedListener
     * which routes the tab selection of the tagged images tab
     * to the presenter.
     */
    private fun generateTabSelectedListener(): TabLayout.OnTabSelectedListener{

        return object: TabLayout.OnTabSelectedListener{

            //unused
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            //unused
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab?.position == 1)

                    presenter.onTaggedImageTabSelected()
            }
        }
    }

    //MARK: -------- INITIALISATION

    //MARK: --------- PRESENTER UPDATES

    /**
     * displays a snackbar with
     * the specified [message]
     */
    override fun displayAlert(message: String) {

        Snackbar.make(window.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * selects the tab in the [tabLayout]
     * at the specified [index]
     */
    override fun selectTabAt(index: Int) {

        tabLayout.getTabAt(index)?.select()
    }

    //MARK: --------- PRESENTER UPDATES

    //MARK: --------- LIFE CYCLE

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }

    //MARK: --------- LIFE CYCLE
}