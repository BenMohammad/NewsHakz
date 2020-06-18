package com.benmohammad.newshakz.data.network

import android.content.Context
import androidx.lifecycle.Transformations.map
import com.benmohammad.newshakz.Mockable
import com.benmohammad.newshakz.R
import com.benmohammad.newshakz.data.model.Item
import com.benmohammad.newshakz.data.room.ItemStore
import io.reactivex.Observable
import retrofit2.Response
import timber.log.Timber
import java.lang.IllegalStateException
import kotlin.math.min

@Mockable
class HackerNewsInteractor(
    private val context: Context,
    private val service: HackerNewsApiService,
    private val store: ItemStore
    ) {

    class LoaderStoriesRequest
    class LoaderStoriesResponse(val request: LoaderStoriesRequest, val items: List<Item>)

    class LoadCommentsRequest(val ids: List<Int>)
    class LoadCommentsResponse(val request: LoadCommentsRequest, val items: List<Item>)

    fun loadStories(request: LoaderStoriesRequest): Observable<LoaderStoriesResponse> {
        return service.getTopStories()
            .toObservable()
            .map { response -> checkResponse(response, context.getString(R.string.error)) }
            .map { response -> response.body() ?: emptyList() }
            .flatMap { ids -> loadItemsFromIds(ids) }
            .map{
                stories -> store.insertItems(stories)
                LoaderStoriesResponse(request, stories)
            }

            .doOnError{
                error -> Timber.e(error)
            }
    }

    private fun loadItemsFromIds(ids: List<Int>): Observable<List<Item>> {

        val allObservables: MutableList<Observable<Item>> = mutableListOf()
        ids.forEach{allObservables.add(loadItem(it))}
        return Observable.zip(allObservables.take(min(ids.size, 50)))
        {
            t -> convertToListofItems(t)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertToListofItems(array: Array<Any>): List<Item> {
        return array.toList() as List<Item>
    }

    private fun loadItem(id: Int): Observable<Item> {
        return service.getItem(id).toObservable()
            .map{
                response -> checkResponse(response, context.getString(R.string.error)) }
            .map {
                response -> response.body()
            }
    }

    fun loadComments(request: LoadCommentsRequest): Observable<LoadCommentsResponse> {
        return loadItemsFromIds(request.ids)
            .map{comments ->
                store.insertItems(comments)
                LoadCommentsResponse(request, comments)
            }
            .doOnError{error -> Timber.e(error)}
    }

    private fun <T> checkResponse(response: Response<T>, message: String):Response<T> {
        return when{
            response.isSuccessful -> response
            else -> throw IllegalStateException(message)
        }
    }}