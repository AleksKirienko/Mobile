package lab2.tabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_three.*

class ActivityThree : AppCompatActivity() {

    val catNames = arrayOf(
        "Рыжик", "Барсик", "Муся", "Васька",
        "Пушок", "Кузьмич", "Шархан"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, catNames)

        // устанавливаем адаптер списку
        listView.adapter = adapter
    }
}
