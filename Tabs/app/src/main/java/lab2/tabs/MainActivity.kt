package lab2.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var openButton: Button? = null
    private var listButton: Button? = null
    private var lastButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        openButton = findViewById(R.id.button1)
        listButton = findViewById(R.id.button2)
        lastButton = findViewById(R.id.button3)

        openButton!!.setOnClickListener {
            //startActivityForResult(Intent(this, activityTwo::class.java), ACTIVITY_TWO_REQUEST_CODE)
            val openAct = Intent(this, activityTwo::class.java)
            startActivity(openAct)
        }

        listButton!!.setOnClickListener {
            val list = Intent(this, ActivityThree::class.java)
            startActivity(list)
        }
    }
}
