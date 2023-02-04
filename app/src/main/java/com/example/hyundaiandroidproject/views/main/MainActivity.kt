package com.example.hyundaiandroidproject.views.main

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hyundaiandroidproject.R
import com.example.hyundaiandroidproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnActivityMainSearch.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.btnActivityMainSearch -> {
                // startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }

}