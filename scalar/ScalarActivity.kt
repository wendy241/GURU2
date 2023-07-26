package com.example.guru2_team3

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class ScalarActivity : AppCompatActivity() {
    lateinit var inputButton: Button
    lateinit var modifyButton: Button
    lateinit var scalar_weightEditText: EditText

    private fun saveData(weight: Int){
        var pref = this.getPreferences(0)
        var editor = pref.edit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.putInt("KEY_WEIGHT",
                scalar_weightEditText.text.toString()!!.toInt()).apply()
        }
    }

    private fun loadData(){
        var pref = this.getPreferences(0)
        var weight = pref.getInt("KEY_WEIGHT", 0)

        if(weight != 0){
            scalar_weightEditText.setText(weight.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scalar)

        inputButton = findViewById<Button>(R.id.inputButton)
        modifyButton = findViewById<Button>(R.id.modifyButton)
        scalar_weightEditText = findViewById<EditText>(R.id.scalar_weightEditText)

        loadData()

        inputButton.setOnClickListener {
            saveData(scalar_weightEditText.text.toString()!!.toInt())

            var intent = Intent(this, ResultActivity::class.java)

            intent.putExtra("weight", scalar_weightEditText.text.toString())
            startActivity(intent)
        }

        modifyButton.setOnClickListener {
            saveData(scalar_weightEditText.text.toString()!!.toInt())

            var intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("weight", scalar_weightEditText.text.toString())
            startActivity(intent)
        }
    }
}
