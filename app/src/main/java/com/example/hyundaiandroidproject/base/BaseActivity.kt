package com.example.hyundaiandroidproject.base

import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {

    lateinit var binding : B

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }


    protected fun hideSoftKeyboard(windowToken : IBinder) {
        // 별도의 변수 선언 없이 획득한 인스턴스의 범위 내에서 작업을 수행한다.
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(windowToken, 0)
        }
    }

    protected fun collapseSearchView(menuSearch : MenuItem) {
        menuSearch.collapseActionView()
    }



    protected fun updateTitle(query: String) {
        supportActionBar?.run {
            subtitle = query
        }
    }

}