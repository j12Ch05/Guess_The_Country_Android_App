package com.example.guessthecountry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guessthecountry.databinding.ActivityEndlessBinding
import org.json.JSONArray
import java.lang.reflect.Field

class EndlessActivity(): AppCompatActivity() {
    private lateinit var binding: ActivityEndlessBinding
    private val countries: List<Map<String, String>> by lazy { gettingData() }
    lateinit var answer: Map<String,String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndlessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(countries.isNotEmpty()){
            answer= countries.random()
            val imageName = answer["id"]?.lowercase() ?: ""

            val imageRes = resources.getIdentifier(imageName,"drawable",packageName)

            if(imageRes!=0){
                binding.flagImage.setImageResource(imageRes)
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
            )

            dataset.add(m1)
        }

        return dataset
    }


}