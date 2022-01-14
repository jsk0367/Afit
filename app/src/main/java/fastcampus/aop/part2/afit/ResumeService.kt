package fastcampus.aop.part2.afit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ResumeService {
    @FormUrlEncoded
    @POST("cv_service/")
    fun requestResume(
        @Field("input1") input1:String
    ) : Call<Resume>
}