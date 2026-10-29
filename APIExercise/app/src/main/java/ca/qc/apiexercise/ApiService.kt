package ca.qc.apiexercise

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //ToDos
    @GET("todos")
    suspend fun getTodos(): List<Todo>
    @GET("todos")
    suspend fun getTodosByUser(@Query("userId") userId: Int): List<Todo>
    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): Todo

    //Post
    @GET("posts")
    suspend fun getPost(): List<Post>
    @GET("posts")
    suspend fun getPostByUser(@Query("userId") userId: Int): List<Post>
    @GET("posts/{id}")
    suspend fun getPostByID(@Path("id") id: Int): Post
}