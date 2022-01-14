package fastcampus.aop.part2.afit


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.net.CookieManager
import java.util.concurrent.TimeUnit


open class LoginActivity : AppCompatActivity() {



    lateinit var id:EditText
    lateinit var password:EditText
    lateinit var login:Button
    lateinit var btnRegister: Button
    lateinit var nonmember: Button


    final val RC_SIGN_IN = 1
    lateinit var signInButton: SignInButton
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var intent = Intent(this, MenuActivity::class.java)

        id = findViewById(R.id.loginId)
        password = findViewById(R.id.loginPassword)
        login = findViewById(R.id.login)
        btnRegister = findViewById(R.id.btnregister)
        nonmember = findViewById(R.id.nonmember)

        var loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        okhttp_client = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS).build()

        nonmember.setOnClickListener{
            Intent(this, MenuActivity::class.java).run{
                startActivity(this)
            }
        }


        var client = okhttp_client


        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.37.56.9/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create((SignService::class.java))

        login.setOnClickListener{
            val idStr = id.text.toString()
            val pwStr = password.text.toString()
            service.login(idStr, pwStr).enqueue(object :Callback<LoginResponse>{

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                  val result = response.body()
//                    Log.e("로그인","${result}")
                    val login = response.body()

                    if (idStr == login?.user_id)
                    {
                        Toast.makeText(this@LoginActivity, "로그인 성공" , Toast.LENGTH_LONG).show()
                        startActivity(intent)
                    }

                    if (idStr == "") {
                        Toast.makeText(this@LoginActivity, "아이디는 공백이 될 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                    if (pwStr  == "") {
                        Toast.makeText(this@LoginActivity, "비밀번호는 공백이 될 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    if (login?.user_id == null)

//                    if (idStr !== login?.user_id){
//                            Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
//                    }
                        Toast.makeText(this@LoginActivity, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    Log.e("로그인실패","${t.localizedMessage}")
//                    Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("실패!")
                    dialog.setMessage("통신에 실패했습니다")
                    dialog.show()

                }
            })






        }

        btnRegister.setOnClickListener{
            Intent(this, RegisterActivity::class.java).run{
                startActivity(this)
            }
        }
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        signInButton = findViewById(R.id.sign_in_button)
//        signInButton.setSize(SignInButton.SIZE_STANDARD)
//
//        signInButton.setOnClickListener {
//
//            signIn()
//        }
    }

//    private fun signIn() {
//        var signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//
//            startActivity(Intent(this, MenuActivity::class.java))
//        }
//    }

//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            val email = account?.email.toString()
//            val familyName = account?.familyName.toString()
//            val givenName = account?.givenName.toString()
//            val displayName = account?.displayName.toString()
//
//            Log.d("account", email)
//            Log.d("account", familyName)
//            Log.d("account", givenName)
//            Log.d("account", displayName)
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("failed", "signInResult:failed code=" + e.statusCode)
}
//    }
//}

interface SignService {
    @FormUrlEncoded
    @POST("/login/app_login")
    fun login(@Field("user_id") user_id:String,
              @Field("password") password:String) : Call<LoginResponse>

}

//interface SignoutService{
//    @GET("/login/logout")
//    Call<Object> getTest();
//
//}