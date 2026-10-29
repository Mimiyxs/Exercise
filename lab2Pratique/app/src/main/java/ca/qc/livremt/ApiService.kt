package ca.qc.livremt

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //ToDos
    @GET("livres")
    suspend fun getLivres(): List<Livre>

}