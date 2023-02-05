package com.example.hyundaiandroidproject.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData<Boolean>(false)
    private val _isError = MutableLiveData<Boolean>(false)
    private val _isErrorMessage = MutableLiveData<String>("")
    val isLoading: LiveData<Boolean> get() = _isLoading
    val isError: LiveData<Boolean> get() = _isError
    val isErrorMessage: MutableLiveData<String> get() = _isErrorMessage

    fun showError(message: String) {
        _isError.value = true
        _isErrorMessage.value = message ?: "No Message"
    }

    fun hideError() {
        _isError.value = false
        _isErrorMessage.value = ""
    }

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}