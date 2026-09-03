package com.example.guessthecountry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthecountry.databinding.ActivityPlayingBinding

class PlayingActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityPlayingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val diff: Int = intent.getIntExtra("diff",1)
    }


    private fun gettingData():List<Map<String,String>>{

    }
}