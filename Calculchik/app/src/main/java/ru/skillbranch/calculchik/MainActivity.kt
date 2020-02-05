package ru.skillbranch.calculchik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var a: Long = 0
    var b: Long = 0
    var c: Long = 0
    var d: Double = 0.0

    //val text1: EditText = findViewById(R.id.first)
    //val text2: EditText = findViewById(R.id.second)
    //val res: TextView = findViewById(R.id.result)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener {
            getFields()
            c = a + b
            result.text = c.toString()
        }
        button2.setOnClickListener {
            getFields()
            c = a - b
            result.text = c.toString()
        }
        button3.setOnClickListener {
            getFields()
            c = a * b
            result.text = c.toString()
        }
        button4.setOnClickListener {
            getFields()
            if (b == 0L)
                Toast.makeText(this, "Деление на 0!", Toast.LENGTH_SHORT).show()
            else {
                d = a / b.toDouble()
                result.text = d.toString()
            }
        }
    }
    fun getFields() {
        a = first.text.toString().toLong()
        b = second.text.toString().toLong()
    }

}
