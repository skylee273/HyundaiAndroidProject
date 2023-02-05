package com.example.hyundaiandroidproject.views.search

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.hyundaiandroidproject.api.model.MovieEntity
import com.example.hyundaiandroidproject.api.provideApi
import com.example.hyundaiandroidproject.base.BaseViewModel
import com.example.hyundaiandroidproject.extentsion.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val _movieList = MutableLiveData<MutableList<MovieEntity>?>()
    val movieList: MutableLiveData<MutableList<MovieEntity>?> get() = _movieList

    private val api by lazy {
        provideApi()
    }

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
            }
            .doOnTerminate {
                hideProgress()
            }
            .subscribe({ items ->
                _movieList.value = items.toMutableList()

            }) {
                showError(it.message!!)
            }
    }

}