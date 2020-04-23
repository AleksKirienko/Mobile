package ru.sibsutis.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

val Names = arrayOf("Sherlock", "John", "Jim", "Iren", "Molly")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DBHelper(this)
        val tl = findViewById<TableLayout>(R.id.tl)
        tl.isShrinkAllColumns = true
        val readBtn = findViewById<Button>(R.id.btn_read)
        var readCount = 0
        var sortCount = 0
        for (i in 0..4) {
            val student = User(
                Names[i],
                Random.nextInt(18, 30), //age
                Random.nextInt(60, 100), //weight
                Random.nextInt(160, 210) //height
            )
            db.insertData(student)
        }

        readBtn.setOnClickListener {
            sortCount = 0
            readCount++
            if (readCount <= 1) {

                val students = db.readData()
                if (students.isEmpty()) {
                    Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show()
                } else {
                    students.trimToSize()
                    for (i in 0..4) {
                        val tr = TableRow(this)
                        tr.layoutParams = TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        tr.gravity = Gravity.CENTER

                        val tv01 = TextView(this)
                        tv01.text = students[i].id.toString()
                        tv01.textSize = 20f
                        tv01.setPadding(16, 16, 16, 16)
                        tr.addView(tv01)

                        val tv02 = TextView(this)
                        tv02.text = students[i].name
                        tv02.textSize = 20f
                        tv02.setPadding(16, 16, 16, 16)
                        tr.addView(tv02)

                        val tv03 = TextView(this)
                        tv03.text = students[i].age.toString()
                        tv03.textSize = 20f
                        tv03.setPadding(16, 16, 16, 16)
                        tr.addView(tv03)

                        val tv04 = TextView(this)
                        tv04.text = students[i].weight.toString()
                        tv04.textSize = 20f
                        tv04.setPadding(16, 16, 16, 16)
                        tr.addView(tv04)

                        val tv05 = TextView(this)
                        tv05.text = students[i].height.toString()
                        tv05.textSize = 20f
                        tv05.setPadding(16, 16, 16, 16)
                        tr.addView(tv05)

                        tl.addView(tr)
                    }

                    val tr2 = TableRow(this)
                    tr2.layoutParams = TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    tr2.gravity = Gravity.CENTER

                    val tv11 = TextView(this)
                    tv11.text = ""
                    tv11.textSize = 20f
                    tv11.setPadding(16, 16, 16, 16)
                    tr2.addView(tv11)

                    val tv12 = TextView(this)
                    tv12.text = ""
                    tv12.textSize = 20f
                    tv12.setPadding(16, 16, 16, 16)
                    tr2.addView(tv12)

                    val tv13 = TextView(this)
                    tv13.text = ""
                    tv13.textSize = 20f
                    tv13.setPadding(16, 16, 16, 16)
                    tr2.addView(tv13)

                    val tv14 = TextView(this)
                    tv14.text = ""
                    tv14.textSize = 20f
                    tv14.setPadding(16, 16, 16, 16)
                    tr2.addView(tv14)

                    val tv15 = TextView(this)
                    tv15.text = ""
                    tv15.textSize = 20f
                    tv15.setPadding(16, 16, 16, 16)
                    tr2.addView(tv15)

                    tl.addView(tr2)
                }
            }

        }

        btn_sort.setOnClickListener {
            db.updateData()
            btn_read.performClick()
        }

        /*btn_sort.setOnClickListener {
            readCount = 0
            sortCount++
            if (sortCount > 1) {
            } else {
                val students = db.sort()
                if (students.isEmpty()) {
                    Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show()
                } else {
                    for (i in 0..4) {
                        val tr = TableRow(this)

                        tr.layoutParams = TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        tr.gravity = Gravity.CENTER

                        val tv01 = TextView(this)
                        tv01.text = students[i].id.toString()
                        tv01.textSize = 20f
                        tv01.setPadding(16, 16, 16, 16)
                        tr.addView(tv01)

                        val tv02 = TextView(this)
                        tv02.text = students[i].name
                        tv02.textSize = 20f
                        tv02.setPadding(16, 16, 16, 16)
                        tr.addView(tv02)

                        val tv03 = TextView(this)
                        tv03.text = students[i].age.toString()
                        tv03.textSize = 20f
                        tv03.setPadding(16, 16, 16, 16)
                        tr.addView(tv03)

                        val tv04 = TextView(this)
                        tv04.text = students[i].weight.toString()
                        tv04.textSize = 20f
                        tv04.setPadding(16, 16, 16, 16)
                        tr.addView(tv04)

                        val tv05 = TextView(this)
                        tv05.text = students[i].height.toString()
                        tv05.textSize = 20f
                        tv05.setPadding(16, 16, 16, 16)
                        tr.addView(tv05)

                        tl.addView(tr)
                    }

                    val tr2 = TableRow(this)
                    tr2.layoutParams = TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    tr2.gravity = Gravity.CENTER

                    val tv11 = TextView(this)
                    tv11.text = ""
                    tv11.textSize = 20f
                    tv11.setPadding(16, 16, 16, 16)
                    tr2.addView(tv11)

                    val tv12 = TextView(this)
                    tv12.text = ""
                    tv12.textSize = 20f
                    tv12.setPadding(16, 16, 16, 16)
                    tr2.addView(tv12)

                    val tv13 = TextView(this)
                    tv13.text = ""
                    tv13.textSize = 20f
                    tv13.setPadding(16, 16, 16, 16)
                    tr2.addView(tv13)

                    val tv14 = TextView(this)
                    tv14.text = ""
                    tv14.textSize = 20f
                    tv14.setPadding(16, 16, 16, 16)
                    tr2.addView(tv14)

                    val tv15 = TextView(this)
                    tv15.text = ""
                    tv15.textSize = 20f
                    tv15.setPadding(16, 16, 16, 16)
                    tr2.addView(tv15)

                    tl.addView(tr2)
                }
            }
        }*/

        btn_del.setOnClickListener {
            db.deleteDB()
            btn_read.performClick()
            // Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
        }
    }
}
