package fastcampus.aop.part2.afit

import retrofit2.Call
import retrofit2.http.*

interface LogoutService {
    @GET("/login/logout")
    fun getLogout(): Call<Logout>
}