package fastcampus.aop.part2.afit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var logout: ConstraintLayout
    lateinit var startexer: ConstraintLayout
    lateinit var startexer2: ConstraintLayout
    lateinit var askbtn: ConstraintLayout
//    lateinit var logout2: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        logout = findViewById(R.id.logout)
        startexer = findViewById(R.id.startexer)
        startexer2 = findViewById(R.id.startexer2)
        askbtn = findViewById(R.id.askbtn)


        startexer.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()

        }

        startexer2.setOnClickListener {
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
            finish()

        }



        askbtn.setOnClickListener {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
            finish()

        }





        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        var client = okhttp_client

        var retrofit = Retrofit.Builder()
            .baseUrl("http://3.37.56.9/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        var logoutService = retrofit.create(LogoutService::class.java)

        var intent = Intent(this, LoginActivity::class.java)

        logout.setOnClickListener {



            logoutService.getLogout().enqueue(object : Callback<Logout> {
                override fun onResponse(call: Call<Logout>, response: Response<Logout>) {


                    Toast.makeText(this@MenuActivity,"로그아웃 성공", Toast.LENGTH_LONG).show()
                    startActivity(intent)
                }

                override fun onFailure(call: Call<Logout>, t: Throwable) {

                }
            })



        }






    }
}




