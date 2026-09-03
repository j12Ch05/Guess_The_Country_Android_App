package com.example.guessthecountry

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthecountry.databinding.ActivityMainBinding
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0

    // Dynamically fetch all drawable IDs ending with "_hidden"
    private val flagDrawables by lazy { getHiddenFlags() }

    private val flagAnimationRunnable = object : Runnable {
        override fun run() {
            if (flagDrawables.isNotEmpty()) {
                // Fixed: Use binding directly instead of uninitialized flagImageView
                binding.myImageView.setImageResource(flagDrawables[currentIndex])
                currentIndex = (currentIndex + 1) % flagDrawables.size
            }
            handler.postDelayed(this, 150)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.post(flagAnimationRunnable)

        binding.btStart.setOnClickListener { gotoCategory() }
        binding.btEndless.setOnClickListener { gotoEndless() }
    }

    private fun gotoCategory() {
        val intent = Intent(this, CategoryActivity::class.java)
        handler.removeCallbacks(flagAnimationRunnable)
        startActivity(intent)
        finish()
    }

    private fun gotoEndless() {
        val intent = Intent(this, EndlessActivity::class.java)
        handler.removeCallbacks(flagAnimationRunnable)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(flagAnimationRunnable)
    }

    private fun getHiddenFlags(): List<Int> {
        val hiddens = mutableListOf<Int>()
        val fields: Array<Field> = R.drawable::class.java.fields

        for (f in fields) {
            try {
                if (f.name.endsWith("_hidden")) {
                    hiddens.add(f.getInt(null))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return hiddens
    }
}