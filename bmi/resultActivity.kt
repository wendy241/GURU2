package com.example.guru2_team3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {


    lateinit var resultTextView: TextView
    lateinit var imageView: ImageView

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultTextView = findViewById<TextView>(R.id.textView)
        var height = intent.getStringExtra("height")!!.toInt()
        var weight = intent.getStringExtra("weight")!!.toInt()


        var bmi = weight / Math.pow(height / 100.0, 2.0)
        val barView = findViewById<ImageView>(R.id.barView)
        val barLabel = findViewById<TextView>(R.id.barLabel)
        var recordTextView = findViewById<TextView>(R.id.recordTextView)
        // BMI 지수에 따라 막대의 색상 변경
        val barColor = when {
            bmi < 18.5 -> R.color.bmi_below_18_5
            bmi < 23 -> R.color.bmi_18_5_to_23
            bmi < 25 -> R.color.bmi_23_to_25
            bmi < 30 -> R.color.bmi_25_to_30
            else -> R.color.bmi_above_30
        }

        val barText = when {
            bmi < 18.5 -> "저체중"
            bmi < 23 -> "정상"
            bmi < 25 -> "과체중"
            bmi < 30 -> "비만"
            else -> "고도비만"
        }


        fun getCurrentDate(): String{
            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val currentDate = Date()
            return dateFormat.format(currentDate)
        }
        val currentDate = getCurrentDate()
        recordTextView.text = "$currentDate     $bmi($barText)"



        // 막대 그래프의 배경 색상 설정
        barView.setBackgroundResource(barColor)

        // 막대 그래프에 해당하는 텍스트 표시
        barLabel.text = "BMI: $bmi"

    }

}
