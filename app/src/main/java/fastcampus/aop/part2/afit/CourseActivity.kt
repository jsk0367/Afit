package fastcampus.aop.part2.afit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.widget.ImageButton
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_course.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class CourseActivity : AppCompatActivity() {

    lateinit var back: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        back = findViewById(R.id.back)

        back.setOnClickListener{

            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
            finish()

        }

        //HTML 적용
        text.text = HtmlCompat.fromHtml(getString(R.string.click), HtmlCompat.FROM_HTML_MODE_COMPACT)
        //Transform 정의
        val transform =
            Linkify.TransformFilter(object : Linkify.TransformFilter, (Matcher, String) -> String {
                override fun transformUrl(p0: Matcher?, p1: String?): String {
                    return ""
                }

                override fun invoke(p1: Matcher, p2: String): String {
                    return ""
                }

            })
        //링크달 패턴 정의
        val pattern1 = Pattern.compile("Click!")

        Linkify.addLinks(text, pattern1, "https://www.youtube.com/c/GYMJONGKOOK", null, transform)


    }
}