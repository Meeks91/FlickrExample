package com.example.micah.tigerspikeflickr.FlickrActivity

import com.example.micah.rxRecyclerViewArrayListAdaper.RxRecyclerViewArrayList
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrApiResponse
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.FlickrImageModel
import com.example.micah.tigerspikeflickr.FlickrActivity.model.dataModels.api.FlickrApiHelper
import com.example.micah.tigerspikeflickr.FlickrActivity.view.activity.FlickrActivityDelegate
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.EventType
import com.example.micah.tigerspikeflickr.GlobalModels.RxBus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

/**
 * Created by Micah on 02/12/2017.
 */
class FlickrPresenter(private val flickrApiHelper: FlickrApiHelper, private val delegate: FlickrActivityDelegate, private val disposable: CompositeDisposable) {

    var imagesRxArrayList = RxRecyclerViewArrayList<FlickrImageModel>()
    var taggedImagesRxArrayList = RxRecyclerViewArrayList<FlickrImageModel>()
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