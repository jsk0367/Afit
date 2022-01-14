package fastcampus.aop.part2.afit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class LungeActivity : AppCompatActivity() {

    lateinit var back: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lunge)

        back = findViewById(R.id.back)

        back.setOnClickListener{

            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}