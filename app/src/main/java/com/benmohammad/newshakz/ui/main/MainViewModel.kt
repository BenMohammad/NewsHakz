package com.benmohammad.newshakz.ui.main

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.databinding.Bindable
import com.benmohammad.newshakz.BR
import com.benmohammad.newshakz.data.model.Item
import com.benmohammad.newshakz.data.network.HackerNewsInteractor
import com.benmohammad.newshakz.ui.NavigationEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    app: Application,
    private val hackerNewsInteractor: HackerNewsInteractor
) : BaseViewModel(app){

    sealed class Stories {
        class Loading: Stories()
        class Result(val stories: List<Item>) :Stories()
        class Error(val message: String) : Stories()
    }
     sealed class Comments{
         class Loading: Comments()
         class Result(val comments: List<Item>): Comments()
         class Error(val message: String): Comments()
     }

    lateinit var item: Item

    @Bindable
    fun isLoading(): Boolean = state is Stories.Loading

    @Bindable
    fun getStories() = state.let{
        when(it) {
            is Stories.Result -> it.stories
            else -> emptyList()
        }
    }

    @Bindable
    fun getComments() = commentsState.let {
        when(it) {
            is Comments.Result -> it.comments
            else -> emptyList()

        }
    }

    @VisibleForTesting
    internal var state: Stories = Stories.Loading()
        set(value) {
        field = value
        notifyPropertyChanged(BR.loading)
        notifyPropertyChanged(BR.stories)
    }

    var commentsState: Comments = Comments.Loading()
        set(value) {
        field = value
        notifyPropertyChanged(BR.loading)
        notifyPropertyChanged(BR.comments)
    }

    fun fetchTopStories() {
        state = Stories.Loading()
        disposables.add(hackerNewsInteractor.loadStories(HackerNewsInteractor.LoaderStoriesRequest())
            .map{it.items}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {state = Stories.Result(it)},
                {state = Stories.Error(it.message!!)}
            ))
    }

    fun fetchComments() {
        commentsState = Comments.Loading()
            item.kids?.let {
                disposables.add(hackerNewsInteractor.loadComments(
                    HackerNewsInteractor.LoadCommentsRequest(
                        item.kids!!
                    )
                )
                    .map{it.items}
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {commentsState = Comments.Result(it)},
                        {commentsState = Comments.Error(it.message!!)}
                    )
                )
            }

    }

    sealed class StoryNavigation {
        data class OpenWebPage(val url: String): StoryNavigation()
        object OpenStory : StoryNavigation()
        object OpenComments: StoryNavigation()

    }

    val navigationEvent = NavigationEvent<StoryNavigation>()

    fun promptMoreInformation(item: Item) {
        this.item = item
        navigationEvent.value =
            if(!item.url.isNullOrEmpty()) StoryNavigation.OpenWebPage(item.url) else StoryNavigation.OpenStory
    }

    fun startShowComments(item: Item) {
        this.item = item
        navigationEvent.value = StoryNavigation.OpenComments

    }
}