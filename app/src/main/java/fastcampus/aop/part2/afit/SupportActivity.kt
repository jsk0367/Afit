package fastcampus.aop.part2.afit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build.*
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import fastcampus.aop.part2.afit.BoardResponse as BoardResponse1


class SupportActivity : LoginActivity() {

    lateinit var cancel: Button
    lateinit var send: Button
    lateinit var title: EditText
    lateinit var email: EditText
    lateinit var writTextView: EditText




    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)



        cancel= findViewById(R.id.cancel)
        send = findViewById(R.id.send)
        email = findViewById(R.id.email)
        title = findViewById(R.id.title)
        writTextView = findViewById(R.id.writeTextview)
//
//        private fun setCookie(){
//            val webView = WebView(this) // this = context
//            val cookieManager = CookieManager.getInstance()
//            cookieManager.acceptCookie()
//
//            val domain = "https://www.yourdomain.com/"
//
//            webView.webViewClient = WebViewClient()
//            webView.settings.javaScriptEnabled = true
//
//            cookieManager.setCookie(domain,"$cookieKey=$cookieValue")
//            cookieManager.setAcceptThirdPartyCookies(webView, true)
//
//            webView.loadUrl(domain)
//        }

//
//            var loggingInterceptor = HttpLoggingInterceptor()
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//
//
//            fun Context.createCookieStore(name: String, persistent: Boolean) = if (persistent) {
//                SharedPreferencesCookieStore(applicationContext, name)
//            } else {
//                InMemoryCookieStore(name)
//            }
//
//            val cookieManager = CookieManager(
//                createCookieStore(name = "myCookies", persistent = true),
//                CookiePolicy.ACCEPT_ALL
//            )
//
//            CookieManager.setDefault(cookieManager)
//
//            okhttp_client = OkHttpClient.Builder()
//                .cookieJar(JavaNetCookieJar(CookieManager()))
//                .addInterceptor(loggingInterceptor)
//                .readTimeout(30, TimeUnit.SECONDS).build()
//
//
//            var client = okhttp_client
//
//



        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.37.56.9/")
            .addConverterFactory(GsonConverterFactory.create())
            //.client(client)
            .build()

        val service = retrofit.create((BoardService::class.java))

        cancel.setOnClickListener{
            val intent = Intent(
                applicationContext,  // ?????? ????????? ????????????
                MenuActivity::class.java
            ) // ?????? ????????? ????????? ??????
            startActivity(intent)
        }

        send.setOnClickListener{
            val titleStr = title.text.toString()
            val emailStr = email.text.toString()
            val writeTextViewStr = writTextView.text.toString()
//                val phoneStr = phonenum.text.toString()
//            val emailStr = email.text.toString()

//,nameStr, phoneStr,emailStr
            service.board(titleStr, emailStr,writeTextViewStr ).enqueue(object : Callback<BoardResponse1> {


                override fun onResponse(
                    call: Call<BoardResponse1>,
                    response: Response<BoardResponse1>
                ) {


                    val result = response.body()
                    Log.e("????????? ?????????????????????","${result}")
                    Toast.makeText(
                        this@SupportActivity,
                        "?????????????????????.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<BoardResponse1>, t: Throwable) {
                    Log.e("????????? ?????????????????????","${t.localizedMessage}")
                }
            })
//
//                val intent = Intent(
//                    applicationContext,  // ?????? ????????? ????????????
//                    MenuActivity::class.java
//                ) // ?????? ????????? ????????? ??????
//                startActivity(intent)
        }
    }
}


interface BoardService{

    @FormUrlEncoded
    @Streaming
    @POST("/qnaboard/regist_qna")
    fun board(@Field("title") user_id:String,
              @Field("email") email:String,
              @Field("content") content:String)
//                 @Field("name") name:String,
//                 @Field("phonenum") phonenum:String,
//                 @Field("email") email:String)

            : Call<BoardResponse1>
}


//interface SendService {
//    @FormUrlEncoded
//    @POST("/login/app_login")
//    fun login(@Field("title") user_id:String,
//              @Field("email") email:String),
//              @Field("content") content:String)
//
//
//    : Call<BoardResponse>
//
//}
//class BoardActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_board)
//    }
//}