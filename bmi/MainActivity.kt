package com.example.guru2_team3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.view.View

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputButton = findViewById<Button>(R.id.inputButton)
        modifyButton = findViewById<Button>(R.id.inputButton)
        heightEditText = findViewById<EditText>(R.id.heightEditText)
        weightEditText = findViewById<EditText>(R.id.weightEditText)

        loadData()

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
    }
}
