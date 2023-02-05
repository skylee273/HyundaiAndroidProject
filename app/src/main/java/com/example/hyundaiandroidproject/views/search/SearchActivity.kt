package com.example.hyundaiandroidproject.views.search

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import com.example.hyundaiandroidproject.R
import com.example.hyundaiandroidproject.api.model.MovieEntity
import com.example.hyundaiandroidproject.api.provideApi
import com.example.hyundaiandroidproject.base.BaseActivity
import com.example.hyundaiandroidproject.databinding.ActivitySearchBinding
import com.example.hyundaiandroidproject.extentsion.plusAssign
import com.jakewharton.rxbinding4.widget.queryTextChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException


class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search), SearchAdapter.ItemClickListener{

    private lateinit var menuSearch: MenuItem
    private lateinit var searchView: SearchView

    private val api by lazy {
        provideApi()
    }

    private val adapter by lazy {
        // apply() 함수를 사용하여 객체 생성과 함수 호출을 한번에 수행한다.
        SearchAdapter().apply {
            setItemClickListener(this@SearchActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menuSearch = menu.findItem(R.id.menu_activity_search_query)

        searchView = (menuSearch.actionView as SearchView)
        searchView.queryTextChangeEvents()
            .filter { it.isSubmitted }
            .map { it.queryText }
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                updateTitle(query)
                hideSoftKeyboard()
                collapseSearchView()
                searchMovie(query)
            }

        with(menuSearch) {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                    if ("" == searchView.query) {
                        finish()
                    }
                    return true
                }
            })
        }
        menuSearch.expandActionView()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.menu_activity_search_query == item.itemId) {
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchMovie(query: String) {
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
            .doOnSubscribe{
                clearResults()
                hideError()
                showProgress()
            }
            .doOnTerminate{ hideProgress()}
            .subscribe({ itmes ->
                with(adapter) {
                    setMovieItems(items)
                    notifyDataSetChanged()
                }
            }) {
                showError(it.message ?: "No Message")
            }
    }

    private fun updateTitle(query: String) {
        // 별도의 변수 선언 없이
        // getSupportActionBar()의 반환값이 널이 아닌 경우에만 작업을 수행한다.
        supportActionBar?.run {
            subtitle = query
        }
    }

    private fun hideSoftKeyboard() {
        // 별도의 변수 선언 없이 획득한 인스턴스의 범위 내에서 작업을 수행한다.
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(searchView.windowToken, 0)
        }
    }

    private fun collapseSearchView() {
        menuSearch.collapseActionView()
    }

    private fun showProgress() {
        binding.pbActivitySearch.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.pbActivitySearch.visibility = View.GONE
    }

    private fun showError(message: String) {
        with(binding.tvActivitySearchMessage) {
            text = message ?: "Unexpected error."
            visibility = View.VISIBLE
        }
    }

    private fun hideError() {
        with(binding.tvActivitySearchMessage) {
            text = ""
            visibility = View.GONE
        }
    }

    override fun onItemClick(repository: MovieEntity?) {
        // apply() 함수를 사용하여 객체 생성과 extra를 추가하는 작업을 동시에 수행한다.
       /* val intent = Intent(this, RepositoryActivity::class.java).apply {
            putExtra(RepositoryActivity.KEY_USER_LOGIN, repository!!.owner.login)
            putExtra(RepositoryActivity.KEY_REPO_NAME, repository.name)
        }
        startActivity(intent)*/
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearResults() {
        // with() 함수를 사용하여 adapter 범위 내에서 작업을 수행한다.
        with(adapter) {
            clearItems()
            notifyDataSetChanged()
        }
    }

}