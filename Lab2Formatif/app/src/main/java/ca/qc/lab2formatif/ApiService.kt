package ca.qc.lab2formatif

import ca.qc.lab2formatif.Data.ReponseLivre;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ApiService {
    @GET("search.json")
    suspend fun rechercherLivres(
        @Query("q") query: String): ReponseLivre

}