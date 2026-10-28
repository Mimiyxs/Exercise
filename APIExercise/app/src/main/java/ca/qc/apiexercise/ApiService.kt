package ca.qc.apiexercise

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>
    @GET("todos")
    suspend fun getTodosByUser(@Query("userId") userId: Int): List<Todo>
    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): Todo
}