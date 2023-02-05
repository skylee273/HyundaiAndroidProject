package com.example.hyundaiandroidproject.views.search

import android.annotation.SuppressLint
import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hyundaiandroidproject.api.model.MovieEntity
import com.example.hyundaiandroidproject.api.provideApi
import com.example.hyundaiandroidproject.base.BaseViewModel
import com.example.hyundaiandroidproject.extentsion.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val _serverMessage = MutableLiveData<MessageSet>()
    private val _movieList = MutableLiveData<MutableList<MovieEntity>?>()

    private val api by lazy {
        provideApi()
    }
    val serverMessage: LiveData<MessageSet> get() = _serverMessage
    val movieList: MutableLiveData<MutableList<MovieEntity>?> get() = _movieList

    @SuppressLint("NotifyDataSetChanged")
    fun searchMovie(query: String) {
        compositeDisposable += api.getSearchMovie(query)
            .flatMap {
                if (it.total == 0) {
                    Single.error(IllegalStateException("No Search result"))
                } else {
                    Single.just(it.movies)
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showProgress()
                hideError()
                _serverMessage.value = MessageSet.LOADING
            }
            .doOnTerminate {
                hideProgress()
            }
            .subscribe({ items ->
                _movieList.value = items.toMutableList()
                _serverMessage.value = MessageSet.SUCCESS

            }) {
                showError(it.message!!)
            }
    }

    enum class MessageSet {
        ERROR,
        SUCCESS,
        LOADING
    }

}