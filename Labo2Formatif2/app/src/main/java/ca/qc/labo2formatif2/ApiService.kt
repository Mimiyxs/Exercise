package ca.qc.labo2formatif2

import ca.qc.labo2formatif2.Data.Livre
import ca.qc.labo2formatif2.Data.ReponseLivre
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search.json")
    suspend fun getLivres(@Query("q" ) query: String): ReponseLivre
}