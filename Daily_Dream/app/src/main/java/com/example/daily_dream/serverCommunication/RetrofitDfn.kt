package com.example.daily_dream.serverCommunication

import com.example.daily_dream.serverCommunication.serverData.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

public class RetrofitClient() {
    private val baseURL: String = "http://ec2-13-209-201-89.ap-northeast-2.compute.amazonaws.com:3000"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseURL) // 요청을 보낼 base url 설정
                .addConverterFactory(GsonConverterFactory.create()) // JSON 파싱을 위한 GsonConverterFactory 추가
                .build()
        }

        return retrofit
    }
}

interface ServiceApi {
    @POST("/user/login")
    suspend fun userLogin(@Body data: LoginData?): LoginResponse?

    @POST("/user/join")
    suspend fun userJoin(@Body data: JoinData?): JoinResponse?

    @POST("/post/posting")
    suspend fun posting(@Body data: PostData?): PostingResponse?

    @POST("/post/commenting")
    suspend fun commenting(@Body data: CommentData?): CommentingResponse?

    @GET("/post/getPosts")
    suspend fun getPosts(@Query("ID") id: Int, @Query("typeOfPost") type: Int): GetPostsResponse?

    @GET("/post/getPost")
    suspend fun getPost(@Query("ID") id: Int): GetPostResponse?

    @GET("/post/getComments")
    suspend fun getComments(@Query("ID") id: Int): GetCommentsResponse?

    @GET("/post/searchPosts")
    suspend fun searchPosts(@Query("SearchWord") searchWord: String, @Query("Id") id: Int, @Query("typeOfPost") typeOfPost: Int): SearchPostResponse?
}