package com.example.guessthecountry

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.guessthecountry.databinding.ActivityEndlessBinding
import org.json.JSONArray
import java.lang.reflect.Field
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EndlessActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityEndlessBinding
    private val countries: List<Map<String, String>> by lazy { gettingData() }
    private val used: ArrayList<String?> = arrayListOf()
    private lateinit var answer: Map<String,String>
    private var buttonNumber: Int = 0
    private var currScore:Int = 0
    private var highScore:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndlessBinding.inflate(layoutInflater)
        setContentView(binding.root)



        loadQuestion()
        val buttons = listOf(binding.btAns1, binding.btAns2, binding.btAns3, binding.btAns4,)

        for (button in buttons) {
            button.setOnClickListener {
                val selectedAnswer = button.text.toString()
                val correctAnswer = answer["country"]

                if (selectedAnswer == correctAnswer) {
                    currScore++
                    if (currScore > countries.size){
                        val intent: Intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    if (currScore > highScore){
                        saveHighScore(currScore)
                    }
                    binding.currentScore.text = currScore.toString()
                    loadQuestion()
                } else {
                    val redColor = Color.parseColor("#8F0404")
                    button.backgroundTintList = ColorStateList.valueOf(redColor)
                    lifecycleScope.launch {

                        delay(500)


                        val intent = Intent(this@EndlessActivity, MainActivity::class.java)
                        startActivity(intent)


                        finish()
                    }
                }
            }
        }

    }

    // Save high score to storage
    private fun saveHighScore(score: Int) {
        val sharedPref = getSharedPreferences("QuizPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("HIGH_SCORE", score)
        editor.apply()
    }

    // Retrieve high score (returns 0 if none saved yet)
    private fun getHighScore(): Int {
        val sharedPref = getSharedPreferences("QuizPrefs", MODE_PRIVATE)
        return sharedPref.getInt("HIGH_SCORE", 0)
    }

    private fun loadQuestion(){
        highScore = getHighScore()
        binding.highScore.text = "$highScore"
        if(countries.isNotEmpty()){
            answer= countries.filterNot{ it["id"] in used}.random()
            used.add(answer["id"])
            val imageName = answer["id"]?.lowercase() ?: ""

            val imageRes = resources.getIdentifier(imageName,"drawable",packageName)

            if(imageRes!=0){
                binding.flagImage.setImageResource(imageRes)
            }

            buttonNumber = (1..4).random()
        }

        if(buttonNumber !=0){
            val used1:ArrayList<String> = arrayListOf()
            when(buttonNumber){
                1 -> {
                    binding.btAns1.text = answer["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns2.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns3.text = countries.filter{it["difficulty"] == answer["difficulty"] }.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                    used1.add(binding.btAns3.text.toString())
                    binding.btAns4.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                }
                2 -> {
                    binding.btAns2.text = answer["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns1.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns3.text = countries.filter{it["difficulty"] == answer["difficulty"] }.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                    used1.add(binding.btAns3.text.toString())
                    binding.btAns4.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                }
                3 -> {
                    binding.btAns3.text = answer["country"] ?: ""
                    used1.add(binding.btAns3.text.toString())
                    binding.btAns1.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns2.text = countries.filter{it["difficulty"] == answer["difficulty"] }.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns4.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                }
                4 -> {
                    binding.btAns4.text = answer["country"] ?: ""
                    used1.add(binding.btAns4.text.toString())
                    binding.btAns1.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns2.text = countries.filter{it["difficulty"] == answer["difficulty"] }.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns3.text = countries.filter{it["difficulty"] == answer["difficulty"]}.filterNot { it["country"] in used1 }.random()["country"] ?: ""
                }

            }
        }
    }

    private fun gettingData(): List<Map<String,String>>{
        val jsonString:String = resources.openRawResource(R.raw.countries_dataset)
            .bufferedReader().use{it.readText()}

        val dataset = mutableListOf<Map<String,String>>()

        val jsonArray = JSONArray(jsonString)
        for(i in 0 until jsonArray.length()){
            val countryObject = jsonArray.getJSONObject(i)

            val m1 = mapOf<String,String>(
                "id" to countryObject.getString("id"),
                "country" to countryObject.getString("country"),
                "capital" to countryObject.getString("capital"),
                "region" to countryObject.getString("region"),
                "difficulty" to countryObject.getString("difficulty")
            )

            dataset.add(m1)
        }

        return dataset
    }


}