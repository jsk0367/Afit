package fastcampus.aop.part2.afit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ImageButton
import android.widget.TextView

class SquatsActivity : AppCompatActivity() {

    lateinit var back: ImageButton
    lateinit var textSquat: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_squats)
        back = findViewById(R.id.back)
        textSquat = findViewById(R.id.textSquat)

        textSquat.setMovementMethod(ScrollingMovementMethod())



        back.setOnClickListener{

            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
            finish()

        }



    }
}