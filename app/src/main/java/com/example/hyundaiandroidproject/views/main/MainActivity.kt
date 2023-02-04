package com.example.hyundaiandroidproject.views.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.example.hyundaiandroidproject.R
import com.example.hyundaiandroidproject.base.BaseActivity
import com.example.hyundaiandroidproject.databinding.ActivityMainBinding
import com.example.hyundaiandroidproject.views.search.SearchActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding.btnActivityMainSearch.setOnClickListener { moveSearchActivity() }
    }

    private fun moveSearchActivity(){
        startActivity(Intent(this@MainActivity, SearchActivity::class.java))
    }


}