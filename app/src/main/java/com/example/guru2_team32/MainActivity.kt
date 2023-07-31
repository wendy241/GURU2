package com.example.guru2_team32

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var inputButton: Button
    lateinit var modifyButton: Button
    lateinit var heightEditText: EditText
    lateinit var weightEditText: EditText

    private fun saveData(height: Int, weight: Int){
        var pref = this.getPreferences(0)
        var editor = pref.edit()

        editor.putInt("KEY_HEIGHT",
            heightEditText.text.toString()!!.toInt()).apply()
        editor.putInt("KEY_WEIGHT",
            weightEditText.text.toString()!!.toInt()).apply()
    }

    private fun loadData(){
        var pref = this.getPreferences(0)
        var height = pref.getInt("KEY_HEIGHT", 0)
        var weight = pref.getInt("KEY_WEIGHT", 0)

        if(height != 0 && weight != 0){
            heightEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputButton = findViewById<Button>(R.id.inputButton)
        modifyButton = findViewById<Button>(R.id.modifyButton)
        heightEditText = findViewById<EditText>(R.id.heightEditText)
        weightEditText = findViewById<EditText>(R.id.weightEditText)

        var imageButton1 = findViewById<ImageButton>(R.id.imageButton1)
        var imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        var imageButton3 = findViewById<ImageButton>(R.id.imageButton3)


        loadData()

        imageButton1.setOnClickListener {

             val intent = Intent(this, GraphActivity::class.java)
            //intent.putExtra("weight", weightEditText.text.toString())
            startActivity(intent)
            }



        imageButton3.setOnClickListener {

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        inputButton.setOnClickListener {
            saveData(heightEditText.text.toString()!!.toInt(),
                weightEditText.text.toString()!!.toInt())

            var intent = Intent(this, ResultActivity::class.java)


            intent.putExtra("height", heightEditText.text.toString())
            intent.putExtra("weight", weightEditText.text.toString())
            startActivity(intent)
        }

        modifyButton.setOnClickListener {
            saveData(
                heightEditText.text.toString()!!.toInt(),
                weightEditText.text.toString()!!.toInt()
            )

            var intent = Intent(this, ResultActivity::class.java)

            intent.putExtra("height", heightEditText.text.toString())
            intent.putExtra("weight", weightEditText.text.toString())
            startActivity(intent)

        }
    }


    fun openActivity_result(view: View){
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
    }

}