package com.example.guessthecountry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthecountry.databinding.ActivityEndlessBinding

class EndlessActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityEndlessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndlessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}