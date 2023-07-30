package com.example.guru2_team32

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private var weightValue: Float = 0.0f

    private fun openActivity_graph() {
        val intent = Intent(this, GraphActivity::class.java)
        intent.putExtra("weight", weightValue.toString()) // weight 값을 String으로 전달
        startActivity(intent)
    }
    private fun openActivity_main() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var inputButton = findViewById<Button>(R.id.inputButton)
        var modifyButton = findViewById<Button>(R.id.modifyButton)
        var textView = findViewById<TextView>(R.id.textView)
        var textView2 = findViewById<TextView>(R.id.textView2)
        var recordTextView = findViewById<TextView>(R.id.recordTextView)
        var barLabel = findViewById<TextView>(R.id.barLabel)

        weightValue = (intent.getStringExtra("weight")?.toFloatOrNull() ?: 0.0).toFloat()

        var imageButton1 = findViewById<ImageButton>(R.id.imageButton1)
        var imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        var imageButton3 = findViewById<ImageButton>(R.id.imageButton3)

        imageButton1.setOnClickListener {
            openActivity_graph()
        }

        imageButton3.setOnClickListener {
            openActivity_main()
        }


        resultTextView = findViewById<TextView>(R.id.textView)

        var height = intent.getStringExtra("height")!!.toInt()
        var weight = intent.getStringExtra("weight")!!.toInt()

        var bmi = weight / Math.pow(height / 100.0, 2.0)
        val barView = findViewById<ImageView>(R.id.barView)
        // BMI 지수에 따라 막대의 색상 변경
        val barColor = ContextCompat.getColor(this, when {
            bmi < 18.5 -> R.color.bmi_below_18_5
            bmi < 23 -> R.color.bmi_18_5_to_23
            bmi < 25 -> R.color.bmi_23_to_25
            bmi < 30 -> R.color.bmi_25_to_30
            else -> R.color.bmi_above_30
        })

        resultTextView.text = "BMI 결과: $bmi"

        val barText = when {
            bmi < 18.5 -> "저체중"
            bmi < 23 -> "정상"
            bmi < 25 -> "과체중"
            bmi < 30 -> "비만"
            else -> "고도비만"
        }

        fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val currentDate = Date()
            return dateFormat.format(currentDate)
        }

        val currentDate = getCurrentDate()
        recordTextView.text = "$currentDate     $bmi($barText)"

        // 막대 그래프의 배경 색상 설정
        barView.setBackgroundColor(barColor)

        // 막대 그래프에 해당하는 텍스트 표시
        barLabel.text = "BMI: $bmi"


    }

}

