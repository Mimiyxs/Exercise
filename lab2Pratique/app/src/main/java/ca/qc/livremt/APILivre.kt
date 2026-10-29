package ca.qc.livremt

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = " https://openlibrary.org/"

    private val retrofit by lazy {
        // Ajout d'un interceptor pour les logs
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(

                json.asConverterFactory("application/json".toMediaType())
            )
            .client(client)
            .build()
    }
    val apiService: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }

}