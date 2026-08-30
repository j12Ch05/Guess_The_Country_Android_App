package com.example.guessthecountry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthecountry.databinding.ActivityCategoryBinding

class GategoryActivity(): AppCompatActivity() {
    private lateinit var binding : ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}