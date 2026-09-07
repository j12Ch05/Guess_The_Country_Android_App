package com.example.guessthecountry

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthecountry.databinding.ActivityCategoryBinding
import java.lang.reflect.Field

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
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
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.post(flagAnimationRunnable)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Re-launch InitialActivity explicitly
                val intent = Intent(this@CategoryActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
                finish()
            }
        })

        val buttons = listOf(binding.btEasy,binding.btMedium,binding.btHard,binding.btVeryHard)

        for(button in buttons){
            button.setOnClickListener {
                when(button.text.toString()){
                    "Easy" -> showCountSelectDialog("Easy")
                    "Medium" -> showCountSelectDialog("Medium")
                    "Hard" -> showCountSelectDialog("Hard")
                    "Very Hard" -> showCountSelectDialog("Very Hard")
                }
            }
        }

    }

    private fun showCountSelectDialog(diff:String){
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_count,null)

        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val bt1 = dialogView.findViewById<Button>(R.id.bt1)
        val bt2 = dialogView.findViewById<Button>(R.id.bt2)
        val bt3 = dialogView.findViewById<Button>(R.id.bt3)

        tvTitle.text = "$diff Mode: Choose Count"

        val buttons = listOf(bt1,bt2,bt3)

        for(button in buttons){
            button.setOnClickListener {
                val intent: Intent = Intent(this, PlayingActivity::class.java)

                when(diff){
                    "Easy" -> intent.putExtra("diff","1")
                    "Medium" -> intent.putExtra("diff","2")
                    "Hard" -> intent.putExtra("diff","3")
                    "Very Hard" -> intent.putExtra("diff","4")
                }

                when(button.text.toString()){
                    "10" -> intent.putExtra("num","10")
                    "20" -> intent.putExtra("num","20")
                    "30" -> intent.putExtra("num","30")
                }
                startActivity(intent)
                dialog.dismiss()
                finish()
            }
        }
        dialog.show()
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