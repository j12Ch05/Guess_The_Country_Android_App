package com.example.guessthecountry

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.guessthecountry.databinding.ActivityPlayingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray

class PlayingActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityPlayingBinding
    private val countries:List<Map<String,String>> by lazy { gettingData() }
    private lateinit var  answer:Map<String,String>
    private var currScore: Int = 0
    private val used: ArrayList<String?> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val diff = intent.getStringExtra("diff")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Re-launch InitialActivity explicitly
                val intent = Intent(this@PlayingActivity, CategoryActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
                finish()
            }
        })

        when(diff){
            "1" -> binding.title.text = "Easy"
            "2" -> binding.title.text = "Medium"
            "3" -> binding.title.text = "Hard"
            "4" -> binding.title.text = "Very Hard"
        }
        loadQuestion(diff)
        val buttons = listOf(binding.btAns1,binding.btAns2,binding.btAns3,binding.btAns4)

        for(button in buttons){
            button.setOnClickListener {
                val selection = button.text.toString()
                val correctAnswer = answer["country"]

                if(selection == correctAnswer){
                    currScore++
                    if(currScore == 10){
                        val intent: Intent = Intent(this, CategoryActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    binding.currentScore.text = "${currScore.toString()}/10"
                    loadQuestion(diff)
                }
                else{
                    val redColor = Color.parseColor("#8F0404")
                    button.backgroundTintList = ColorStateList.valueOf(redColor)
                    lifecycleScope.launch {

                        delay(500)


                        val intent = Intent(this@PlayingActivity, CategoryActivity::class.java)
                        startActivity(intent)


                        finish()
                    }
                }
            }
        }

    }

    private fun loadQuestion(dif:String?){
        var buttonNumber:Int = 0

        if(countries.isNotEmpty()){
            answer = countries.filter { it["difficulty"] == dif }.filterNot { it["id"] in used }.random()
            used.add(answer["id"])
            val imageName = answer["id"]?.lowercase() ?: ""

            val imageRes = resources.getIdentifier(imageName,"drawable",packageName)

            if(imageRes!=0){
                binding.flagImage.setImageResource(imageRes)
            }

            buttonNumber = (1..4).random()
        }

        if(buttonNumber!=0){
            val used1:ArrayList<String> = arrayListOf()
            when(buttonNumber){
                1 ->{
                    binding.btAns1.text = answer["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns2.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns3.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns3.text.toString())
                    binding.btAns4.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                }
                2 ->{
                    binding.btAns2.text = answer["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns1.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns3.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns3.text.toString())
                    binding.btAns4.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                }
                3 ->{
                    binding.btAns3.text = answer["country"] ?: ""
                    used1.add(binding.btAns3.text.toString())
                    binding.btAns1.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns2.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns4.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                }
                4 ->{
                    binding.btAns4.text = answer["country"] ?: ""
                    used1.add(binding.btAns4.text.toString())
                    binding.btAns1.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns1.text.toString())
                    binding.btAns2.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                    used1.add(binding.btAns2.text.toString())
                    binding.btAns3.text = countries.filter{it["difficulty"] == dif}.filterNot{it["country"] in used1}.random()["country"] ?: ""
                }
            }
        }

    }


    private fun gettingData():List<Map<String,String>>{
        val jsonString: String = resources.openRawResource(R.raw.countries_dataset)
            .bufferedReader().use { it.readText() }

        val dataset = mutableListOf<Map<String,String>>()

        val jsonArray = JSONArray(jsonString)
        for(i in 0 until jsonArray.length()){
            val countryObject = jsonArray.getJSONObject(i)

            val m1:Map<String,String> = mapOf(
                "id" to countryObject.getString("id"),
                "country" to countryObject.getString("country"),
                "capital" to countryObject.getString("capital"),
                "region" to countryObject.getString("region"),
                "difficulty" to countryObject.getString("difficulty"),
            )

            dataset.add(m1)
        }
        return dataset
    }
}