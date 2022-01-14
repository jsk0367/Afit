package fastcampus.aop.part2.afit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class RegisterActivity : AppCompatActivity() {


    lateinit var id: EditText
    lateinit var password: EditText
    lateinit var name: EditText
    lateinit var phonenum: EditText
    lateinit var email: EditText
    lateinit var register: Button
    lateinit var password2: EditText
    lateinit var backToHome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        id = findViewById(R.id.textid)
        password = findViewById(R.id.textPassword)

        register = findViewById(R.id.btnregister)
        backToHome = findViewById(R.id.backToHome)



        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.37.56.9:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create((RegisterService::class.java))



        btnregister.setOnClickListener {





            val idStr = id.text.toString()
            val pwStr = password.text.toString()
//            val nameStr = name.text.toString()
//            val phoneStr = phonenum.text.toString()
//            val emailStr = email.text.toString()

//,nameStr, phoneStr,emailStr
            service.register(idStr, pwStr).enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val result = response.body()
                    Log.e("로그인", "${result}")

                }


                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("로그인", "${t.localizedMessage}")
                }
            })

            val intent = Intent(
                applicationContext,  // 현재 화면의 제어권자
                LoginActivity::class.java
            ) // 다음 넘어갈 클래스 지정
            startActivity(intent)
            }








        backToHome.setOnClickListener{
            val intent = Intent(
                applicationContext,  // 현재 화면의 제어권자
                LoginActivity::class.java
            ) // 다음 넘어갈 클래스 지정
            startActivity(intent)
        }
    }
}

interface RegisterService{

    @FormUrlEncoded
    @POST("/login/regist_user")
    fun register(@Field("user_id") user_id:String,
                 @Field("password") password:String)
//                 @Field("name") name:String,
//                 @Field("phonenum") phonenum:String,
//                 @Field("email") email:String)

            : Call<LoginResponse>

}