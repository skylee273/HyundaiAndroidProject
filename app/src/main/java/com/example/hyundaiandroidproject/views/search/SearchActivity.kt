package com.example.hyundaiandroidproject.views.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.example.hyundaiandroidproject.R
import com.example.hyundaiandroidproject.base.BaseActivity
import com.example.hyundaiandroidproject.databinding.ActivitySearchBinding
import com.jakewharton.rxbinding4.widget.queryTextChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers


class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private lateinit var menuSearch: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding.searchViewModel = SearchViewModel(application)
        initAdapter()
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
                hideSoftKeyboard(searchView.windowToken)
                collapseSearchView(menuSearch)
                binding.searchViewModel.searchMovie(query)
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

    private fun initAdapter() {
        searchAdapter = SearchAdapter { movie ->
            Intent(Intent.ACTION_VIEW, Uri.parse(movie.link)).takeIf {
                it.resolveActivity(packageManager) != null
            }?.run(this::startActivity)
        }
        binding.rvActivitySearchList.adapter = searchAdapter


    }

}