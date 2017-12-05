package com.example.micah.tigerspikeflickr.FlickrActivity

import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.FlickrActivity.model.api.FlickrApiDelegate
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrApiResponse
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrImageModel
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivityDelegate
import com.example.micah.tigerspikeflickr.globalModels.RxBus.EventType
import com.example.micah.tigerspikeflickr.globalModels.RxBus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

/**
 * Created by Micah on 02/12/2017.
 */
class FlickrPresenter(private val flickrApiHelper: FlickrApiDelegate, private val delegate: FlickrActivityDelegate, private val cDisposable: CompositeDisposable) {

    var unTaggedImagesRxArrayList = RxRecyclerViewArrayList<FlickrImageModel>()
    var taggedImagesRxArrayList = RxRecyclerViewArrayList<FlickrImageModel>()
    private lateinit var currentSearchTag: String

    init {

        onRequestDetailedImages()

        initRxBusSubscription()
    }

    //MARK: ---------- INITIALISATION

    /**
     * inits the RxBus subscription to
     * receive 'retrieve more images' events
     */
    private fun initRxBusSubscription() {

        RxBus.bus.subscribe{

            when (it.type) {

                EventType.retrieveMoreDetailed -> onRequestDetailedImages()
                EventType.retrieveNewTag -> onRequestRetrieveTaggedImagesUsing(it.data as Int)
                EventType.retrieveMoreCurrentTag -> onRequestToRetrieveMoreCurrentTag()
            }

        }.addTo(cDisposable)
    }

    //MARK: ---------- INITIALISATION

    //MARK: ---------- IMAGE RETRIEVAL OPERATIONS

    /**
     * handles: requesting details images from
     * the Flickr API and routing the responses
     * to the appropriate response handler
     */
    private fun onRequestDetailedImages() {

        flickrApiHelper
                .getAllImages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUntaggedImagesRetrieved, this::onImageRetrievalFailure)
                .addTo(cDisposable)
    }

    /**
     * handles: retrieving tagged images from the Flickr API. The tag
     * is determined by the flickrImageModel at the specified [index] of
     * the unTaggedImagesRxArrayList. If this model has no tag then the delegate
     * is notified and method returns. The API response is routed to the
     * appropriate response handler.
     */
    private fun onRequestRetrieveTaggedImagesUsing(index: Int){

        //unwrap searchTag
        val searchTag = unTaggedImagesRxArrayList[index].searchTag.takeIf {it != ""}

                //short circuit & notify user we can't find other images
                ?: return delegate.displayAlert("No Tag available :(")

        //store currentSearchTag
        currentSearchTag = searchTag

        //execute update:
        flickrApiHelper
                .getImagesWith(searchTag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNewTaggedImagesRetrieved, this::onImageRetrievalFailure)
                .addTo(cDisposable)
    }

    /**
     * handles: retrieving images from the flickr API
     * which match the global [currentSearchTag]. The
     * response is routed to the appropriate response
     * handler.
     */
    private fun onRequestToRetrieveMoreCurrentTag(){

        flickrApiHelper
                .getImagesWith(currentSearchTag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMoreTaggedImagesRetrieved, this::onImageRetrievalFailure)
                .addTo(cDisposable)
    }


    //MARK: ---------- IMAGE RETRIEVAL OPERATIONS

    //MARK: ---------- IMAGE RETRIEVAL RESPONSES


    /**
     * handles: adding all of the images in the specified
     * [flickrApiResponse.flickrModelsArrayLis] to the
     * global [unTaggedImagesRxArrayList] which automaticlly updates the
     * detailed images RecyclerView
     */
    private fun onUntaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        unTaggedImagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)
    }

    /**
     * handles: checking if there any images for the tagged image search.
     * If there is then the global [taggedImagesRxArrayList] is emptied
     * and then populated with the retrieved images. The tagged images tab
     * is then selected. If no images were found then the  delegate is requested
     * to display an alert and notify the user.
     */
    private fun onNewTaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        //check if there are any tagged images
        if (flickrApiResponse.flickrModelsArrayList.size > 0) {

            //update tagged images:
            taggedImagesRxArrayList.clear()
            taggedImagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)

            //go to tagged images tab
            delegate.selectTabAt(1)}

        else {

            delegate.displayAlert("No matching tags found :(")}
    }

    /**
     * handles: checking if any more images the with global [currentSearchTag]
     * were found. If there were then they're added to the global [taggedImagesRxArrayList].
     * If no new images were found then the delegate displays an alert nofifying the user.
     */
    private fun onMoreTaggedImagesRetrieved(flickrApiResponse: FlickrApiResponse){

        //check if there are any tagged images
        if (flickrApiResponse.flickrModelsArrayList.size > 0)

            taggedImagesRxArrayList.addAll(flickrApiResponse.flickrModelsArrayList)

        else

            delegate.displayAlert("No new images found :(")
    }

    /**
     * handles: notifying the user of why we faield to retrieve images
     * with the messaged in the specified [throwable.localizedMessage]
     */
    private fun onImageRetrievalFailure(throwable: Throwable){

        delegate.displayAlert(throwable.localizedMessage)
    }

    //MARK: ---------- IMAGE RETRIEVAL RESPONSES

    //MARK: ---------- TAB SELECTION

    /**
     * handles: requesting the delegate to display an alert
     * to the user if they go on to the tagged images tab
     * if there are are not tagged images to show
     */
    fun onTaggedImageTabSelected() {

        if (taggedImagesRxArrayList.size == 0)

            delegate.displayAlert("No tagged images to show :(")
    }

    //MARK: ---------- TAB SELECTION
}