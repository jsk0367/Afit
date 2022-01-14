package fastcampus.aop.part2.afit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import soup.neumorphism.NeumorphCardView

class ViewActivity : AppCompatActivity() {


    lateinit var cardSquats: NeumorphCardView
    lateinit var cardPunch: NeumorphCardView
    lateinit var cardStretching: NeumorphCardView
    lateinit var cardPushups: NeumorphCardView
    lateinit var cardCourses: NeumorphCardView
    lateinit var home: NeumorphCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)


        cardSquats = findViewById(R.id.cardSquats)
        cardPunch = findViewById(R.id.cardPunch )
        cardStretching = findViewById(R.id.cardStretching)
        cardPushups = findViewById(R.id.cardPushups)
        cardCourses = findViewById(R.id.cardCourses)
        home = findViewById(R.id.home3)

        cardSquats.setOnClickListener {
            val intent = Intent(this, SquatsActivity::class.java)
            startActivity(intent)
            finish()

        }

        cardPunch.setOnClickListener {
            val intent = Intent(this, PlankActivity::class.java)
            startActivity(intent)
            finish()

        }

        cardStretching.setOnClickListener {
            val intent = Intent(this, LungeActivity::class.java)
            startActivity(intent)
            finish()

        }

        cardPushups.setOnClickListener {
            val intent = Intent(this, PushupsActivity::class.java)
            startActivity(intent)
            finish()

        }

        cardCourses.setOnClickListener {
            val intent = Intent(this, CourseActivity::class.java)
            startActivity(intent)
            finish()

        }

        home.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()

        }
    }





}